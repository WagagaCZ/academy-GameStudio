const candyField = document.getElementById("candyField");
const scoresTableBody = document.getElementById("scoresTableBody");

//const serverUrl="http://localhost:8080";
const serverUrl="";//if from the same server as this page

const getBestScoresJsonUrl =serverUrl+"/api/score/candycrush";
const getGameFieldUrl =serverUrl+"/candycrush/json";
const chModeUrl =serverUrl+"/candycrush/jsonchmode";

const numRows = 10; // number of rows in the game field
const numCols = 10; // number of columns in the game field


const TileColor = {
    RED: 'red',
    BLUE: 'blue',
    GREEN: 'green',
    YELLOW: 'yellow',
    PURPLE: 'purple',
    EMPTY: 'empty',
  };
  
  const colors = [TileColor.RED, TileColor.BLUE, TileColor.GREEN, TileColor.YELLOW, TileColor.PURPLE];

fetchGameFieldAndRenderAll(numRows,numCols);

function generateBoard(row,col) {
    const gameField = document.querySelector('#candyField');
    
  
    // Clear existing game field
    gameField.innerHTML = '';
    gameField.id = 'candyField'
    const tileSize = 3; // size of each tile in pixels

    // Generate tiles for the game field
    for (let i = 0; i < numRows; i++) {
      for (let j = 0; j < numCols; j++) {
        const tile = document.createElement('div');
        tile.classList.add('tile');
        tile.style.width = tileSize + 'em';
        tile.style.height = tileSize + 'em';
        tile.style.top = i * tileSize + 'em';
        tile.style.left = j * tileSize + 'em';
        gameField.appendChild(tile);
      }
    }
  }

  function refreshBoard() {
    // Clear the gameField by setting its innerHTML to an empty string
    gameField.innerHTML = '';
  
    // Loop through the board array and generate the HTML for each cell
    for (let i = 0; i < board.length; i++) {
      for (let j = 0; j < board[i].length; j++) {
        // Create a new div element for each cell
        const cell = document.createElement('div');
  
        // Add the 'cell' class to the cell element
        cell.classList.add('cell');
  
        // Set the innerHTML of the cell to the corresponding value from the board array
        cell.innerHTML = board[i][j];
  
        // Append the cell element to the gameField element
        gameField.appendChild(cell);
      }
    }
  
    // Update the display for moves and biggest value
    displayMoves.textContent = moves;
    displayBiggest.textContent = biggest;
  }
  

  function fetchGameFieldAndRenderAll(row, col) {
    let getGameFieldUrlWParams = getGameFieldUrl;
    if (Number.isInteger(row) && Number.isInteger(col)) {
      getGameFieldUrlWParams = `${getGameFieldUrlWParams}?row=${row}&column=${col}`;
      console.log(getGameFieldUrlWParams);
    }
  
    fetch(getGameFieldUrlWParams)
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          return Promise.reject(new Error('Game field acquisition failed. Server answered with status ' + response.status));
        }
      })
      .then(gameFieldData => {
        console.log(gameFieldData);
        renderGameField(gameFieldData, candyField);
        if (gameFieldData.justFinished) {
          fetchAndRenderBestScores();
        }
      })
      .catch(error => {
        console.error(error);
        let errorMessage = "Failed to get or render the game field. Details:" + error;
        candyField.innerHTML = errorMessage;
      })
  }
  
  let firstTile = null;

  function renderGameField(gameFieldData, renderTo) {
    const candyCrushTable = document.createElement('table');
    candyCrushTable.id = 'candyCrushTable';
    candyCrushTable.style.margin = '0 auto';
  
    for (let row = 0; row < gameFieldData.numRows; row++) {
      const tr = document.createElement('tr');
      for (let col = 0; col < gameFieldData.numCols; col++) {
        const tile = gameFieldData.tiles[row][col];
        const td = document.createElement('td');
        td.className = tile.color.toLowerCase();
        td.setAttribute('data-row', row);
        td.setAttribute('data-col', col);
        td.addEventListener('click', () => {
          if (firstTile === null) {
            // This is the first tile that the player has selected
            firstTile = { row, col };
          } else {
            // This is the second tile that the player has selected
            const secondTile = { row, col };
            swapTiles(firstTile, secondTile,gameFieldData);
            firstTile = null;
          }
        });
        tr.appendChild(td);
      }
      candyCrushTable.appendChild(tr);
    }
  
    renderTo.appendChild(candyCrushTable);
  }
  
  function swapTiles(tile1, tile2, gameFieldData) {
    // Swap the tiles in the array
    const tempTile = gameFieldData.tiles[tile1.row][tile1.col];
    gameFieldData.tiles[tile1.row][tile1.col] = gameFieldData.tiles[tile2.row][tile2.col];
    gameFieldData.tiles[tile2.row][tile2.col] = tempTile;
  
    // Check for matches and remove them
    const matches = findMatches(gameFieldData);
    if (matches.length > 0) {
      removeMatches(matches, gameFieldData);
      applyGravity(gameFieldData);
    } else {
      // If no matches were found, swap the tiles back
      gameFieldData.tiles[tile2.row][tile2.col] = gameFieldData.tiles[tile1.row][tile1.col];
      gameFieldData.tiles[tile1.row][tile1.col] = tempTile;
    }
  
    // Re-render the game field
    const candyCrushTable = document.getElementById('candyCrushTable');
    for (let row = 0; row < gameFieldData.numRows; row++) {
      for (let col = 0; col < gameFieldData.numCols; col++) {
        let tile = gameFieldData.tiles[row][col];
        const td = candyCrushTable.rows[row].cells[col];
        if (tile === null) {
          tile = new Tile(TileColor.EMPTY);
          gameFieldData.tiles[row][col] = tile;
        }
        td.className = (tile.color || TileColor.EMPTY).toLowerCase();
      }
    }
  }
  
  
  
  function findMatches(gameFieldData) {
    const matches = [];
  
    // Check for horizontal matches
    for (let row = 0; row < gameFieldData.numRows; row++) {
      let startCol = 0;
      while (startCol < gameFieldData.numCols) {
        const match = getHorizontalMatch(row, startCol, gameFieldData);
        if (match.length < 3) {
          startCol++;
        } else {
          matches.push(match);
          startCol += match.length;
        }
      }
    }
  
    // Check for vertical matches
    for (let col = 0; col < gameFieldData.numCols; col++) {
      let startRow = 0;
      while (startRow < gameFieldData.numRows) {
        const match = getVerticalMatch(startRow, col, gameFieldData);
        if (match.length < 3) {
          startRow++;
        } else {
          matches.push(match);
          startRow += match.length;
        }
      }
    }
  
    return matches;
  }
  
  function getHorizontalMatch(row, col, gameFieldData) {
    const tile = gameFieldData.tiles[row][col];
    if (!tile) return [];
  
    const color = tile.color;
    const match = [{ row, col }];
    let currentCol = col + 1;
  
    while (currentCol < gameFieldData.numCols && gameFieldData.tiles[row][currentCol].color === color) {
      match.push({ row, col: currentCol });
      currentCol++;
    }
  
    return match;
  }
  
  
  function getVerticalMatch(row, col, gameFieldData) {
    const color = gameFieldData.tiles[row][col].color;
    const match = [{ row, col }];
    let currentRow = row + 1;
  
    while (currentRow < gameFieldData.numRows && gameFieldData.tiles[currentRow][col].color === color) {
      match.push({ row: currentRow, col });
      currentRow++;
    }
  
    return match;
  }
  
  function removeMatches(matches, gameFieldData) {
  matches.forEach(match => {
    match.forEach(tile => {
      gameFieldData.tiles[tile.row][tile.col].color = colors.EMPTY;
    });
  });
}


  
function applyGravity(gameFieldData) {
    for (let col = 0; col < gameFieldData.numCols; col++) {
      let emptyCount = 0;
      for (let row = gameFieldData.numRows - 1; row >= 0; row--) {
        const tile = gameFieldData.tiles[row][col];
        if (tile.color === TileColor.EMPTY) {
          emptyCount++;
        } else if (emptyCount > 0) {
          gameFieldData.tiles[row + emptyCount][col] = tile;
          gameFieldData.tiles[row][col] = new Tile(TileColor.EMPTY);
          emptyCount = 0;
        }
      }
    }
  
    // Fill any empty spaces with new tiles
    for (let col = 0; col < gameFieldData.numCols; col++) {
      for (let row = 0; row < gameFieldData.numRows; row++) {
        const tile = gameFieldData.tiles[row][col];
        if (tile.color === TileColor.EMPTY) {
          gameFieldData.tiles[row][col] = new Tile(getRandomColor());
        }
      }
    }
  }
  
  
  function fillEmptyTiles(gameFieldData) {
    for (let row = 0; row < gameFieldData.numRows; row++) {
      for (let col = 0; col < gameFieldData.numCols; col++) {
        const tile = gameFieldData.tiles[row][col];
        if (tile && tile.color === TileColor.EMPTY) {
          // Generate a random color for the empty tile
          const randomColor = getRandomColor();
          gameFieldData.tiles[row][col] = new Tile(randomColor);
        }
      }
    }
  }
  
  function getRandomColor(colors) {
    const randomIndex = Math.floor(Math.random() * colors.length);
    return colors[randomIndex];
  }
  
  class Tile {
    constructor(color) {
      this.color = color;
    }
  
    getColor() {
      return this.color;
    }
  
    setColor(color) {
      this.color = color;
    }
  }
  
  function getTileText(tile) {
    switch (tile.color) {
      case TileColor.RED:
        return "R";
      case TileColor.BLUE:
        return "B";
      case TileColor.GREEN:
        return "G";
      case TileColor.YELLOW:
        return "Y";
      case TileColor.PURPLE:
        return "P";
      default:
        return "E";
    }
  }
  
  function getTileClass(tile) {
    switch (tile.color) {
      case TileColor.RED:
        return "red-tile";
      case TileColor.BLUE:
        return "blue-tile";
      case TileColor.GREEN:
        return "green-tile";
      case TileColor.YELLOW:
        return "yellow-tile";
      case TileColor.PURPLE:
        return "purple-tile";
      default:
        return "empty-tile";
    }
  }
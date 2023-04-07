const candyField = document.getElementById("candyField");
const scoresTableBody = document.getElementById("scoresTableBody");

//const serverUrl="http://localhost:8080";
const serverUrl = "";//if from the same server as this page

const getBestScoresJsonUrl = serverUrl + "/api/score/candycrush";
const getGameFieldUrl = serverUrl + "/candycrush/json";
const chModeUrl = serverUrl + "/candycrush/jsonchmode";

const numRows = 10; // number of rows in the game field
const numCols = 10; // number of columns in the game field

let score = 0;
const scoreBox = document.getElementById('score');

function updateScore(newScore) {
  score = newScore;
  scoreBox.innerHTML = `<span id="score">${score}</span>`;
}

updateScore(0); 


const TileColor = {
    RED: 'red',
    BLUE: 'blue',
    GREEN: 'green',
    YELLOW: 'yellow',
    PURPLE: 'purple',
    EMPTY: 'empty',
};

const colors = [TileColor.RED, TileColor.BLUE, TileColor.GREEN, TileColor.YELLOW, TileColor.PURPLE];

fetchGameFieldAndRenderAll(numRows, numCols);

function generateBoard(row, col) {
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
                    swapTiles(firstTile, secondTile, gameFieldData);
                    firstTile = null;
                }
            });
            tr.appendChild(td);
        }
        candyCrushTable.appendChild(tr);
    }

    renderTo.appendChild(candyCrushTable);
}


//TODO: AFTER A COUPLE OF SWAPS, GAMEFIELD SEEMS TO BE BROKEN,NEED TO FIX
function swapTiles(tile1, tile2, gameFieldData) {
    // Swap the tiles in the array
    const tempTile = gameFieldData.tiles[tile1.row][tile1.col];
    gameFieldData.tiles[tile1.row][tile1.col] = gameFieldData.tiles[tile2.row][tile2.col];
    gameFieldData.tiles[tile2.row][tile2.col] = tempTile;

    // Check for matches and remove them
    let matches = findMatches(gameFieldData);
    if (matches.length > 0) {
        removeMatches(matches, gameFieldData);
        applyGravity(gameFieldData); // apply gravity after removing matches
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
    firstTile = null;
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
    let match = [{ row, col }];
    let currentCol = col + 1;
  
    // Look for matching tiles to the right
    while (currentCol < gameFieldData.numCols && gameFieldData.tiles[row][currentCol].color === color) {
      match.push({ row, col: currentCol });
      currentCol++;
    }
  
    currentCol = col - 1;
  
    // Look for matching tiles to the left
    while (currentCol >= 0 && gameFieldData.tiles[row][currentCol].color === color) {
      match.unshift({ row, col: currentCol });
      currentCol--;
    }
  
    if (match.length >= 3) {
      return match;
    }
  
    // If there are fewer than three tiles in the match, check for additional matches to the left or right
    let matches = [match];
  
    // Check for additional matches to the right
    while (currentCol < gameFieldData.numCols - 1) {
      currentCol++;
  
      if (gameFieldData.tiles[row][currentCol].color === color) {
        const additionalMatch = [{ row, col: currentCol }];
        let nextCol = currentCol + 1;
  
        while (nextCol < gameFieldData.numCols && gameFieldData.tiles[row][nextCol].color === color) {
          additionalMatch.push({ row, col: nextCol });
          nextCol++;
        }
  
        currentCol = nextCol - 1;
  
        if (additionalMatch.length >= 3) {
          matches.push(additionalMatch);
        }
      } else {
        break;
      }
    }
  
    currentCol = col - 1;
  
    // Check for additional matches to the left
    while (currentCol > 0) {
      currentCol--;
  
      if (gameFieldData.tiles[row][currentCol].color === color) {
        const additionalMatch = [{ row, col: currentCol }];
        let nextCol = currentCol - 1;
  
        while (nextCol >= 0 && gameFieldData.tiles[row][nextCol].color === color) {
          additionalMatch.unshift({ row, col: nextCol });
          nextCol--;
        }
  
        currentCol = nextCol + 1;
  
        if (additionalMatch.length >= 3) {
          matches.push(additionalMatch);
        }
      } else {
        break;
      }
    }
  
    // Return the longest match
    return matches.sort((a, b) => b.length - a.length)[0] || [];
  }
  




function getVerticalMatch(row, col, gameFieldData) {
    const tile = gameFieldData.tiles[row][col];
    if (!tile) return [];

    const color = tile.color;
    const match = [{ row, col }];
    let currentRow = row + 1;

    // Look for matching tiles below
    while (currentRow < gameFieldData.numRows && gameFieldData.tiles[currentRow][col].color === color) {
        match.push({ row: currentRow, col });
        currentRow++;
    }

    // Look for matching tiles above
    currentRow = row - 1;
    while (currentRow >= 0 && gameFieldData.tiles[currentRow][col].color === color) {
        match.push({ row: currentRow, col });
        currentRow--;
    }

    return match;
}


function removeMatches(matches, gameFieldData) {
    matches.forEach(match => {
      match.forEach(tile => {
        gameFieldData.tiles[tile.row][tile.col].color = TileColor.EMPTY;
      });
      const matchLength = match.length;
      if (matchLength === 3) {
        score += 100;
      } else if (matchLength === 4) {
        score += 150;
      } else if (matchLength === 5) {
        score += 500;
      } else if (matchLength > 5) {
        score += 1000;
      }
    });
    updateScore(score);
  }
  
//   function printGamefieldData() {
//     console.log("Gamefield Data:");
//     for (let row = 0; row < ROWS; row++) {
//       let rowData = "";
//       for (let col = 0; col < COLS; col++) {
//         rowData += gamefieldData[row][col] + " ";
//       }
//       console.log(rowData);
//     }
//   }
  


function applyGravity(gameFieldData) {
  // Iterate over columns from left to right
  for (let col = 0; col < gameFieldData.numCols; col++) {
    let emptyTiles = 0;

    // Iterate over rows from bottom to top
    for (let row = gameFieldData.numRows - 1; row >= 0; row--) {
      const tile = gameFieldData.tiles[row][col];

      // If the current tile is empty, increment the count of empty tiles
      if (!tile || tile.color === TileColor.EMPTY) {
        emptyTiles++;
      } else if (emptyTiles > 0) {
        // If there are empty tiles below the current tile, move it down
        const newRow = row + emptyTiles;
        gameFieldData.tiles[newRow][col] = tile;
        gameFieldData.tiles[row][col] = new Tile(TileColor.EMPTY);

        // Update the DOM
        const tdAbove = document.querySelector(`[data-row='${newRow}'][data-col='${col}']`);
        const td = document.querySelector(`[data-row='${row}'][data-col='${col}']`);
        td.className = tdAbove.className;
        tdAbove.className = TileColor.EMPTY;
      }
    }

    // If there are any empty tiles in the column, fill them with new random tiles
    for (let i = 0; i < emptyTiles; i++) {
      const newRow = i;
      const newTile = new Tile(getRandomColor());
      gameFieldData.tiles[newRow][col] = newTile;

      // Update the DOM
      const td = document.querySelector(`[data-row='${newRow}'][data-col='${col}']`);
      td.className = newTile.color;
    }
  }

  // Check if there are any matches on the board and call applyGravity recursively if there are
  const matches = findMatches(gameFieldData);
  if (matches.length > 0) {
    removeMatches(matches, gameFieldData);
    applyGravity(gameFieldData);
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

function getRandomColor() {
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
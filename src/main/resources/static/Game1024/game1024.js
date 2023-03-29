const gameField = document.querySelector('.game-field');

const overlayReset = document.querySelector('.overlay-reset');
const overlayLose = document.querySelector('.overlay-lose');
const overlayWin = document.querySelector('.overlay-win');

const displayMoves = document.querySelector('#display-moves');
const displayBiggest = document.querySelector('#display-biggest');

let tiles = [];
let movedOrMerged = false;
let gameStatus = "paused";
let moveCount = 0;
let biggestNumber = 0;


//main
generateBoard();
addRandomNumber();
addRandomNumber();
refreshBoard();
showScores("Game1024");
showComments("Game1024");
showRatings("Game1024");

function generateBoard() {
  for (let i = 0; i < 4; i++) {
    tiles.push([]);
    for (let j = 0; j < 4; j++) {
      tiles[i].push({ value: 0 });
    }
  }
}

function refreshBoard() {
  gameField.innerHTML = '';
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4; j++) {
      const tile = document.createElement('div');
      tile.classList.add('tile');
      tile.classList.add(`tile-${tiles[i][j].value}`);
      if (tiles[i][j].value > 0) {
        tile.innerHTML = tiles[i][j].value;
      }
      tile.addEventListener('click', (event) => incrementTile(i, j, event));
      gameField.append(tile);
    }
  }
  displayMoves.innerHTML = moveCount;
  displayBiggest.innerHTML = biggestNumber;
}

function incrementTile(x, y, event) {
  if (event.shiftKey) {
    if (tiles[x][y].value === 0) {
      tiles[x][y].value++;
    } else {
      tiles[x][y].value *= 2;
    }

    refreshBoard();
  }
}

// keyboard
document.addEventListener('keydown', function (event) {

  if(gameStatus === "paused") {
    startTimer();
    gameStatus = "playing";
  }

  if (gameStatus !== "playing") {
    return;
  }

  movedOrMerged = false;
  switch (event.key) {
    case 'w': moveUp(); break;
    case 's': moveDown(); break;
    case 'a': moveLeft(); break;
    case 'd': moveRight(); break;
  }

  if (movedOrMerged) {
    addRandomNumber();
    moveCount++;
    biggestNumber = getBiggestNumber();
  }

  if (isWon()) {
    refreshBoard();
    pauseTimer();
    sendScoreAndReloadTable("Game1024");
    overlayWin.innerHTML = "You Won👍 \n your score: " + countScore();
    overlayWin.classList.remove('hidden');
    gameStatus = "won";
    return;
  }

  if (isLost()) {
    refreshBoard();
    pauseTimer();
    overlayLose.classList.remove('hidden');
    gameStatus = "lost";
    return;
  }
  refreshBoard();
});

// conditions
function isLost() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i][j].value === 0) {
        return false;
      }
    }
  }
  if (canMergeAtRow() || canMergeAtColumn()) {
    return false;
  }
  return true;
}

function isWon() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i][j].value === 1024) {
        return true;
      }
    }
  }
  return false;
}

function canMergeAtRow() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4 - 1; j++) {
      if (tiles[i][j].value == tiles[i][j + 1].value) {
        return true;
      }
    }
  }
  return false;
}

function canMergeAtColumn() {
  for (let i = 0; i < 4 - 1; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i][j].value == tiles[i + 1][j].value) {
        return true;
      }
    }
  }
  return false;
}
// dispatch move
function dispatchKeyboardEvent(key) {
  const event = new KeyboardEvent('keydown', { key });
  document.dispatchEvent(event);
}

function addRandomNumber() {
  let validTile = false;

  let numberToAdd = Math.floor(Math.random()*11) > 8 ? 2 : 1

  while(!validTile) {
    let randomRow = Math.floor(Math.random()*4)
    let randomCol = Math.floor(Math.random()*4)

    if(tiles[randomRow][randomCol].value === 0) {
      tiles[randomRow][randomCol].value = numberToAdd
      validTile = true;
    }
  }
}

function getBiggestNumber() {
  let biggestNumber = 0;
  for(let i = 0; i < 4; i++) {
    for(let j = 0; j < 4; j++) {
      if(tiles[i][j].value > biggestNumber) {
        biggestNumber = tiles[i][j].value;
      }
    }
  }
  return biggestNumber;
}

function reset() {
  tiles = [];
  gameStatus = "resetting";
  overlayLose.classList.add('hidden');
  overlayWin.classList.add('hidden');
  overlayReset.classList.remove('hidden');

  setTimeout(() => {
  generateBoard();
  addRandomNumber();
  addRandomNumber();
  refreshBoard();
  gameStatus = "paused";
  moveCount = 0;
  biggestNumber = 0;
  overlayReset.classList.add('hidden');
  resetTimer();
  refreshBoard();
  }, 600);

}

function countScore() {
  return 1000 - moveCount - (Math.floor(getTime()/3000));
}
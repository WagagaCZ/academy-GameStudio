console.log("Minesweeper JS loaded");

const gameBoard = document.querySelector(".minesweeper_board");
const mines = document.querySelector("#mines");
const flagged = document.querySelector("#flagged");
const tilesOpened = document.querySelector("#tilesOpen");
const tilesAll = document.querySelector("#tilesAll");

const overlayReset = document.querySelector("#overlay_reset");
const overlayLose = document.querySelector("#overlay_lose");
const overlayWin = document.querySelector("#overlay_win");

const difficulty = document.querySelector("#difficulty");
const setDifficulty = document.querySelector("#setDifficulty");

const scoreTableBody = document.querySelector("#score-table-body");

const commentTableBody = document.querySelector("#comment-table-body");
const commentForm = document.querySelector("#comment-form");
const commentSubmitBtn = document.querySelector("#comment-submit");

const ratingDisplay = document.querySelector("#rating");
const ratingForm = document.querySelector("#rating-select");
const ratingRadios = ratingForm.querySelectorAll("input[type=radio]");
const ratingSubmitBtn = document.querySelector("#rating-button");

let board = [];
let bombs = 4;
let size = 8;
let flaggedTiles = 0;

let started = false;

// main
init();
showScores("Minesweeper");
showComments("Minesweeper");
showRatings("Minesweeper");

function init() {
  // Create game board
  for (let i = 0; i < size; i++) {
    board.push([]);
    for (let j = 0; j < size; j++) {
      board[i].push({ hidden: true, bomb: false, flagged: false, value: 0 });
    }
  }

  // Place bombs randomly
  let bombCount = 0;
  while (bombCount < bombs) {
    const row = Math.floor(Math.random() * size);
    const col = Math.floor(Math.random() * size);
    if (!board[row][col].bomb) {
      board[row][col].bomb = true;
      board[row][col].value = "💣";
      bombCount++;
    }
  }
  // place clues, display
  mines.innerHTML = bombs;
  tilesAll.innerHTML = size * size - bombs;
  generateClues();
  refreshBoard();
}

/////////////
// open tile
function openTile(x, y) {

  if (!started) {
    startTimer()
    started = true;
  }


  if (board[x][y].flagged === true) {
    return;
  }

  // lose
  if (board[x][y].bomb) {
    board[x][y].hidden = false;
    refreshBoard();
    pauseTimer()
    overlayLose.classList.remove("hidden");
    return;
  }

  if (board[x][y].hidden === true) {
    board[x][y].hidden = false;
    if (board[x][y].value === 0) {
      openAdjacentTiles(x, y);
    }
  }

  // win
  if (isSolved()) {
    refreshDisplay();
    pauseTimer();
    overlayWin.textContent = "You won \n Score: " + countScore();
    overlayWin.classList.remove("hidden");
    sendScoreAndReloadTable()
    return;
  }

  refreshBoard();
}

/////////////
// set difficulty

setDifficulty.addEventListener("click", () => {
  if (difficulty.value === "easy") {
    bombs = 4;
    size = 8;
    gameBoard.style.gridTemplateColumns = "repeat(8, 1fr)";
  } else if (difficulty.value === "medium") {
    bombs = 20;
    size = 11;
    gameBoard.style.gridTemplateColumns = "repeat(11, 1fr)";
  } else if (difficulty.value === "hard") {
    bombs = 40;
    size = 15;
    gameBoard.style.gridTemplateColumns = "repeat(15, 1fr)";
  }
  reset();
})

function generateClues() {
  for (let i = 0; i < size; i++) {
    for (let j = 0; j < size; j++) {
      if (board[i][j].value !== "💣") {
        board[i][j].value = countAdjacentMines(i, j);
      }
    }
  }
}

function countAdjacentMines(x, y) {
  let count = 0;
  for (let i = -1; i <= 1; i++) {
    let actRow = x + i;
    if (actRow >= 0 && actRow < size) {
      for (let j = -1; j <= 1; j++) {
        let actCol = y + j;
        if (actCol >= 0 && actCol < size) {
          if (board[actRow][actCol].bomb === true) {
            count++;
          }
        }
      }
    }
  }
  return count;
}

gameBoard.addEventListener("click", (e) => {
  let x = e.target.dataset.row;
  let y = e.target.dataset.col;
  if(x === undefined || y === undefined) { return; }
  openTile(+x, +y);
});

gameBoard.addEventListener("contextmenu", (e) => {
  e.preventDefault();
  let x = +e.target.dataset.row;
  let y = +e.target.dataset.col;

  if (board[x][y].hidden === false) {
    return;
  }

  if (board[x][y].flagged === false) {
    if (flaggedTiles === bombs) {
      return;
    }
    board[x][y].flagged = true;
    flaggedTiles++;
  } else {
    board[x][y].flagged = false;
    flaggedTiles--;
  }
  refreshBoard();
});

function refreshBoard() {
  gameBoard.innerHTML = "";
  for (let i = 0; i < size; i++) {
    for (let j = 0; j < size; j++) {
      const cell = document.createElement("div");
      cell.classList.add("cell");
      cell.dataset.row = i.toString();
      cell.dataset.col = j.toString();
      if (!board[i][j].hidden) {
        cell.innerHTML = board[i][j].value;
        setCellStyle(board[i][j].value, cell);
      }
      if (board[i][j].flagged) {
        cell.innerHTML = "🚩";
        cell.classList.add("cell-flagged");
      }
      gameBoard.appendChild(cell);
    }
  }
  refreshDisplay();
}

function refreshDisplay() {
  flagged.innerHTML = flaggedTiles;
  tilesOpened.innerHTML = countOpenedTiles();
}



function openAdjacentTiles(x, y) {
  for (let i = -1; i <= 1; i++) {
    let actRow = x + i;
    if (actRow >= 0 && actRow < size) {
      for (let j = -1; j <= 1; j++) {
        let actCol = y + j;
        if (actCol >= 0 && actCol < size) {
          if (board[actRow][actCol].hidden === true) {
            openTile(actRow, actCol);
          }
        }
      }
    }
  }
}

function reset() {
  board = [];
  flaggedTiles = 0;
  overlayLose.classList.add("hidden");
  overlayWin.classList.add("hidden");
  overlayReset.classList.remove("hidden");
  resetTimer()
  started = false;
  setTimeout(() => { overlayReset.classList.add("hidden") }, 500);
  init();
  refreshBoard();
}

function setCellStyle(value, cell) {
  switch (value) {
    case 0: { cell.classList.add("cell-0"); break; }
    case 1: { cell.classList.add("cell-1"); break; }
    case 2: { cell.classList.add("cell-2"); break; }
    case 3: { cell.classList.add("cell-3"); break; }
    case 4: { cell.classList.add("cell-4"); break; }
    case 5: { cell.classList.add("cell-5"); break; }
    case 6: { cell.classList.add("cell-6"); break; }
    case 7: { cell.classList.add("cell-7"); break; }
    case 8: { cell.classList.add("cell-8"); break; }
    case "💣": { cell.classList.add("cell-bomb"); break; }
  }
}

function countOpenedTiles() {
  let count = 0;
  for (let i = 0; i < size; i++) {
    for (let j = 0; j < size; j++) {
      if (board[i][j].hidden === false) {
        count++;
      }
    }
  }
  return count;
}

function isSolved() {
  return (size * size) - bombs === countOpenedTiles();
}

function countScore() {
  return (size + size) * bombs - (Math.floor(getTime() / 1000));
}


async function getPlayer() {
  const player = await apiGetUser();
}

/////////////////////////
// score api

async function showScores(game) {
  let scores = await apiGetScores(game);

  // clear table first
  scoreTableBody.innerHTML = '';

  scores.forEach(score => {
    let date = new Date(score.playedOn);
    date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
    scoreTableBody.innerHTML += `
      <tr">
          <td>${score.player}</td>
          <td>${score.points}</td>
          <td>${date}</td>
      </tr>
      `;
  });
}


async function sendScoreAndReloadTable() {
  const res = await apiGetUser();
  const player = res.loggedUser;

  if (player != null) {
    await apiSendScore(player, 'Minesweeper', countScore());
    showScores("Minesweeper")
  }
}

//////////////////////////////
// RATING API
async function showRatings(game) {
  let rating = await apiGetAvgRating(game);
  if (rating > 0) {
    let formattedRating = rating.toFixed(2);
    ratingDisplay.innerHTML = `Rating: <span>${formattedRating}</span>`;
  } else {
    ratingDisplay.innerHTML = `Game not rated yet`;
  }
}

ratingSubmitBtn.addEventListener('click', async () => {

  const res = await apiGetUser();
  const player = res.loggedUser;

  if (player == null) { alert("log in first"); return; }

  let rating = -1;
  ratingRadios.forEach(radio => {
    if (radio.checked) { rating = radio.value; }
    return
  });

  if (rating == -1) { return; }

  await apiSendRating(player, 'Minesweeper', rating);
  animateRating();
  ratingRadios.forEach(radio => {
    if (radio.checked) { radio.checked = false; }
    return
  });
  showRatings("Minesweeper");
});

function animateRating() {
  ratingDisplay.classList.add("animate");
  setTimeout(() => { ratingDisplay.classList.remove("animate") }, 900);
}

// COMMENT API
async function showComments(game) {
  let comments = await apiGetComments(game);
  // clear table first
  commentTableBody.innerHTML = '';

  comments.forEach(comment => {
    let date = new Date(comment.commentedOn);
    date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
    commentTableBody.innerHTML += `
      <tr">
          <td>${comment.comment}</td>
          <td>
            <span><i class="fa-solid fa-user"></i> </span>
            <span>${comment.player}</span>
            <span> &nbsp </span>
            <span><i class="fa-solid fa-calendar-days"></i> </span>
            <span>${date}</span>
          </td>
      </tr>
      `;
  });
}
commentSubmitBtn.addEventListener('click', async () => {
  const player = commentForm[0].value;
  const comment = commentForm[1].value;
  if (comment.length > 0) {

    await apiSendComment(player,'Minesweeper', comment);
    commentForm[0].value = '';
    commentForm[1].value = '';
    showComments('Minesweeper');
  }
});

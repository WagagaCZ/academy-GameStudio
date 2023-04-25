/* mas tu vela funkcii s takmer rovnakym kodom.
da sa to spravit tak, ze ked presuvas dlazdice,
tak si docasne vytiahnes kazdy riadok/stlpec do samostatneho pola a nad nim robis presun.
Lebo presuny sa vlastne deju vzdy nad riadkom/stlpcom a nesuvisia s ostatnym okolim.
Cize nebudes mat moveRight, moveLeft atd, ale len move, ktory bude pre dany riadok robit vzdy to iste (shift, merge, shift)
ale bude to robit na predpripraveny riadok (ak to bude smer hore, tak to bude stlpec otoceny odzadu).

Je to ale pekny kod na ukor vacsieho mnozstva kopirovania hodnot. Aby si si nezaplnil pamat,
tak si treba vytvorit docasne premennu,
kde budes vzdy postupne davat tie stransformovane hodnoty, nie zakazdym nove pole.
 */

////////
// Moves

// RIGHT //////////////////////////////////////////
function moveRight() {
  let move = canMoveRight()
  while (move) {
    switchRight()
    movedOrMerged = true;
    move = canMoveRight();
  }
  mergeRight();
}
function switchRight() {
  for (let i = 0; i < 4; i++) {
    for (let j = 4 - 2; j >= 0; j--) {

      if (tiles[i][j].value > 0 && tiles[i][j + 1].value === 0) {
        tiles[i][j + 1].value = tiles[i][j].value;
        tiles[i][j].value = 0;
      }
    }
  }
}
function canMoveRight() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4 - 1; j++) {
      if (tiles[i][j].value > 0 && tiles[i][j + 1].value === 0) {
        return true;
      }
    }
  }
  return false;
}

function mergeRight() {
  for (let i = 0; i < 4; i++) {
    for (let j = 4 - 1; j > 0; j--) {
      if (tiles[i][j].value > 0 && (tiles[i][j].value === tiles[i][j - 1].value)) {
        tiles[i][j].value *= 2;
        tiles[i][j - 1].value = 0;
        movedOrMerged = true;
      }
    }
  }
  switchRight();
}
// LEFT //////////////////////////////////////////
function moveLeft() {
  let move = canMoveLeft();
  while (move) {
    switchLeft();
    movedOrMerged = true;
    move = canMoveLeft();
  }
  mergeLeft();
}

function switchLeft() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4 - 1; j++) {
      if (tiles[i][j + 1].value > 0 && tiles[i][j].value === 0) {
        tiles[i][j].value = tiles[i][j + 1].value;
        tiles[i][j + 1].value = 0;
      }
    }
  }
}

function canMoveLeft() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4 - 1; j++) {
      if (tiles[i][j + 1].value > 0 && tiles[i][j].value === 0) {
        return true;
      }
    }
  }
  return false;
}

function mergeLeft() {
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 4 - 1; j++) {
      if (tiles[i][j].value > 0 && tiles[i][j].value === tiles[i][j + 1].value) {
        tiles[i][j].value *= 2;
        tiles[i][j + 1].value = 0;
        movedOrMerged = true;
      }
    }
  }
  switchLeft();
}
// Up //////////////////////////////////////////
function moveUp() {
  let move = canMoveUp();
  while (move) {
    switchUp();
    movedOrMerged = true;
    move = canMoveUp();
  }
  mergeUp();
}

function switchUp() {
  for (let i = 0; i < 4 - 1; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i + 1][j].value > 0 && tiles[i][j].value === 0) {
        tiles[i][j].value = tiles[i + 1][j].value;
        tiles[i + 1][j].value = 0;
      }
    }
  }
}

function canMoveUp() {
  for (let i = 0; i < 4 - 1; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i + 1][j].value > 0 && tiles[i][j].value === 0) {
        return true;
      }
    }
  }
  return false;
}

function mergeUp() {
  for (let i = 0; i < 4 - 1; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i][j].value > 0 && tiles[i][j].value === tiles[i + 1][j].value) {
        tiles[i][j].value *= 2;
        tiles[i + 1][j].value = 0;
        movedOrMerged = true;
      }
    }
  }
  switchUp();
}

// Down //////////////////////////////////////////
function moveDown() {
  let move = canMoveDown();
  while (move) {
    switchDown();
    movedOrMerged = true;
    move = canMoveDown();
  }
  mergeDown();
}

function switchDown() {
  for (let i = 4 - 1; i > 0; i--) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i - 1][j].value > 0 && tiles[i][j].value === 0) {
        tiles[i][j].value = tiles[i - 1][j].value;
        tiles[i - 1][j].value = 0;
      }
    }
  }
}

function canMoveDown() {
  for (let i = 0; i < 4 - 1; i++) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i][j].value > 0 && tiles[i + 1][j].value === 0) {
        return true;
      }
    }
  }
  return false;
}

function mergeDown() {
  for (let i = 4 - 1; i > 0; i--) {
    for (let j = 0; j < 4; j++) {
      if (tiles[i][j].value > 0 && tiles[i][j].value === tiles[i - 1][j].value) {
        tiles[i][j].value *= 2;
        tiles[i - 1][j].value = 0;
        movedOrMerged = true;
      }
    }
  }
  switchDown();
}

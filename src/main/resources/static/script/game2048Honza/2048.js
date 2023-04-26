document.addEventListener('keydown',  (e) => {
    //dalo by sa tu pridat event.prevent default v pripade up/down, aby to neskrolovalo, ked sa hra
    switch(e.code) {
        case "ArrowUp": makeMove("UP").then(); break;
        case "ArrowDown": makeMove("DOWN").then(); break;
        case "ArrowLeft": makeMove("LEFT").then(); break;
        case "ArrowRight": makeMove("RIGHT").then(); break;
    }
    // if (e.code === "ArrowUp") {
    //     makeMove("UP").then();
    // } else if (e.code === "ArrowDown") {
    //     makeMove("DOWN").then();
    // } else if (e.code === "ArrowLeft") {
    //     makeMove("LEFT").then();
    // } else if (e.code === "ArrowRight") {
    //     makeMove("RIGHT").then();
    // }
});


const API_URL = "/api/2048";
let field;
let gameEnd = false;

const renderField = () => {
    const mineFieldElem = document.getElementById("minefield");

    renderGameState();
    renderScore();

    //alternativny zapis react-style :)
    let resultTable = `
      <table class="minefield center">
        <tbody>
          ${field.tiles.map((row, ri) => `
            <tr>
              ${row.map((tile, ti) => `
                <td class="${'tile' + field.tiles[ri][ti].value}">
                  ${tile.empty ? '' : tile.value}
                </td>
              `).join('')}
            </tr>
          `).join('')}
        </tbody>
      </table>
    `;

    mineFieldElem.innerHTML = resultTable;
};

const renderScore = () => {
    const scoreField = document.getElementById("scoreCount");
    scoreField.innerHTML = "Score : " + field.score;
};

const renderGameState = () => {
    const stateElem = document.getElementById("gamestate");
    if(field.state === "PLAYING") {
        stateElem.innerHTML = "PLAYING";
        stateElem.style.color = "violet";
    } else if(field.state === "FAILED") {
        stateElem.innerHTML = "YOU LOST!!!";
        stateElem.style.color = "red";
    } else if(field.state === "SOLVED") {
        stateElem.innerHTML = "YOU WON!";
        stateElem.style.color = "green";
    }
};

const newGame = async () => {
    try {
        const response = await fetch(API_URL + "/new?size=4");
        if( response.status === 200 ) {
            field = await response.json();
            renderField();
        }
    } catch ( err ) {
        console.error("Unable to fetch new game field", err);
    }
};

const makeMove = async (direction) => {
    if(gameEnd === true) {
        return;
    }
    try {
        const response = await fetch(API_URL + "/move?dir=" + direction);
        if(response.status === 200) {
            field = await response.json();
            renderField();
        }

    } catch ( err ) {
        console.error("Unable to make move", err);
    }
};

window.onload = newGame();
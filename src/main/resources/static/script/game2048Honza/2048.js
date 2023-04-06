document.addEventListener('keydown',  (e) => {
    if (e.code === "ArrowUp") {
        makeMove("UP").then();
    } else if (e.code === "ArrowDown") {
        makeMove("DOWN").then();
    } else if (e.code === "ArrowLeft") {
        console.log("moving left");
        makeMove("LEFT").then();
    } else if (e.code === "ArrowRight") {
        makeMove("RIGHT").then();
    }
});

const API_URL = "/api/2048";
let field;
let gameEnd = false;

const renderField = () => {
    const mineFieldElem = document.getElementById("minefield");
    renderGameState();
    let resultTable = `<table class="minefield"><tbody>`;
    for( let r = 0; r < 4; ++r ) {
        mineFieldElem.innerHTML += `<tr>`;
        for( let c = 0; c < 4; ++c ) {
            const tile = field.tiles[r][c];
            let AttrClassName = "tile" + tile.value;
            resultTable += `<td class="${AttrClassName}">`;
            if( !tile.empty )
                resultTable += tile.value;
            resultTable += `</td>`;
        }
        resultTable += `</tr>`;
    }
    resultTable += `</tbody></table>`;
    mineFieldElem.innerHTML = resultTable;
};

const renderGameState = () => {
    const stateElem = document.getElementById("gamestate");
    if( field.state === "PLAYING" ) {
        stateElem.innerHTML = "PLAYING";
    } else if( field.state === "FAILED" ) {
        stateElem.innerHTML = "YOU LOST";
    } else if( field.state === "SOLVED" ) {
        stateElem.innerHTML = "YOU WON!";
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

const makeMove = async ( direction ) => {
    if( gameEnd === true ) {
        return;
    }
    try {
        const response = await fetch(API_URL + "/move?dir=" + direction);
        if( response.status === 200 ) {
            field = await response.json();
            renderField();
        }

    } catch ( err ) {
        console.error("Unable to make move", err);
    }
};

window.onload = newGame();
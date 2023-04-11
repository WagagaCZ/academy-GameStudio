// noinspection JSValidateTypes,JSVoidFunctionReturnValueUsed

console.log("Mastermind script loaded");

const PLAY_COLUMNS = document.querySelector('#play-columns');
const DIV_SELECT_COLORS = document.getElementById('div-select-color');
const SUBMIT_BUTTON = document.getElementById('submit-btn');

const COLORS = ['blue', 'yellow', 'orange', 'green', 'violet', 'purple'];
let codeLength = 4;
let tries = 8;

let random_code = [];
let submitTry = 1;

let score;

init();


/**
 * The initial function that prepares the hidden code (of colors),
 * loads all the necessary stuff to the HTML file,
 * and sets the dynamic parts.
 */
function init() {
    random_code = [];
    submitTry = 1;
    PLAY_COLUMNS.innerHTML = '';
    DIV_SELECT_COLORS.innerHTML = '';
    score = codeLength * 20;

    createRandomCode();

    setTryAndCorrectionColumn();
    setColorSelection();
}


/**
 * Tables appear after the window is loaded.
 */
document.addEventListener('readystatechange', event => {

    // When window loaded ( external resources are loaded too- `css`,`src`, etc...)
    if (event.target.readyState === "complete") {
        showScores("Mastermind");
        showComments("Mastermind")
    }
});


/**
 * Prepares the random code, the hidden colors.
 */
function createRandomCode() {
    for (let i = 1; i <= codeLength; i++) {
        let random_color = COLORS[Math.floor(Math.random() * COLORS.length)]
        random_code.push(random_color);
    }

    console.log(random_code);
}


/**
 * Sets the length of the random code, the hidden colors.
 */
function setCodeLength() {
    let newCodeLength = prompt("Please, enter then number of hidden pegs (4-7)", "4");
    while (!"!/^[1-9]+$/.test(newCodeLength)") {
        alert("Unvalid input.");
        newCodeLength = prompt("Type a number between 4 and 7, please.");
    }

    if (newCodeLength > 7) {
        alert("Too high! The number of hidden pegs was set to 7.")
        newCodeLength = 7;
    }

    if (newCodeLength < 4) {
        alert("Too low! The number of hidden pegs was set to 4.")
        newCodeLength = 4;
    }

    codeLength = newCodeLength;
}

/**
 * Sets the number of tries.
 * The range of tries is limited (5-20).
 */
function setNumberOfTries() {
    let numberOfTries = prompt("Please, enter then number of tries (min 5, max 20)", "4");
    while (!"!/^[4-9]+$/.test(newCodeLength)") {
        alert("Unvalid input.");
        numberOfTries = prompt("Type a number between 5 and 20, please.");
    }

    if (numberOfTries > 20) {
        alert("Too high! The number of tries was set to 20.")
        numberOfTries = 20;
    }

    if (numberOfTries < 5) {
        alert("Too low! The number of tries was set to 5.")
        numberOfTries = 5;
    }

    tries = numberOfTries;
}


/**
 * Sets the columns for tries and corrections for the HTML file.
 */
function setTryAndCorrectionColumn() {
    for (let i = 1; i <= tries; i++) {
        let div_try = document.createElement('div');
        div_try.setAttribute('id', 'try-'+i);
        div_try.setAttribute('class', 'try');

        let div_left = document.createElement('div');
        div_left.setAttribute('class', 'left');

        let div_right = document.createElement('div');
        div_right.setAttribute('class', 'right');

        for (let i = 1; i <= codeLength; i++) {
            let div_l = document.createElement('div');
            let div_r = document.createElement('div');
            div_left.append(div_l);
            div_right.append(div_r);
        }

        div_try.append(div_left);
        div_try.append(div_right);
        PLAY_COLUMNS.prepend(div_try);
    }
}


/**
 * Sets the code selection for the HTML file.
 */
function setColorSelection() {
    for (let i = 1; i <= codeLength; i++) {
        let div_select_wrapper = document.createElement('div');
        div_select_wrapper.setAttribute('class', 'select-wrapper');
        let select = document.createElement('select');

        // Colors
        for (let color of COLORS) {
            let option = document.createElement('option');
            option.setAttribute('style', 'background-color:' + color);
            option.setAttribute('value', color);
            select.append(option);
        }

        select.setAttribute('style', 'background-color:' + COLORS[0]);

        select.addEventListener('change', (e) => {
            e.target.setAttribute('style', 'background-color:' + e.target.value)
        });

        div_select_wrapper.append(select);
        DIV_SELECT_COLORS.append(div_select_wrapper);
    }
}


/**
 * Adds an even listener to the submit button.
 * Submits a guess and immediately checks colors and their positions.
 */
SUBMIT_BUTTON.addEventListener('click', () => {
    let input_colors = document.querySelectorAll('.select-wrapper>select');
    let input_colors_arr = [];
    for (let v of input_colors) {
        input_colors_arr.push(v.value);
    }

    show('left', input_colors_arr);

    let correction_array = createCorrectionArray(input_colors_arr);
    show('right', correction_array);

    submitTry++;
    checkWin(correction_array);
});


/**
 * Shows the colors submitted by submit_button.
 * @param type can be 'left' (color input) or 'right' (correction).
 * @param colors is an array of colors to show.
 */
function show(type, colors) {
    let tryView = document
        .querySelectorAll('#try-' + submitTry + '>.' + type + '>div')
    tryView.forEach((v, i) => {
        v.setAttribute('style', 'background-color:' + colors[i]);
    });
}


/**
 * Created the correction array that displays which colors are correctly placed.
 * Color is in solution but not correctly placed -> white
 * Color is correct and placing is correct  -> greenyellow
 * Color isn't in the hidden code -> red
 * @param input_colors_arr is the guess, the array of colors submitted.
 * @returns {*[]} is the array of displaying which colors are correctly placed.
 */
function createCorrectionArray(input_colors_arr) {
    let random_code_copy = [...random_code];
    let correction_array = [];

    for (let i in random_code_copy) {
        if (random_code_copy[i] === input_colors_arr[i]) {
            correction_array[i] = 'greenyellow';
        } else if (random_code_copy.includes(input_colors_arr[i])) {
            correction_array[i] = 'white';
        } else {
            correction_array[i] = 'red'
        }
    }

    return correction_array;
}


/**
 * Check correction_array if all colors are correct.
 * @param correction_array is the array to be checked.
 */
function checkWin(correction_array) {
    let countCorrect = 0;
    for (let v of correction_array) {
        if (v === 'greenyellow') {
            countCorrect++;
        }
    }

    if (countCorrect === codeLength) {
        sendScoreAndReloadTable("Mastermind")
        alert('VICTORY\nYour score is: ' + countMastermindScore());
        init();
    } else if (submitTry > tries) {
        alert('GAME OVER');
        init();
    }
}


/**
 * Lets the user choose the length of hidden code
 * and the number of tries.
 */
function setAndStartNewGame() {
    setCodeLength();
    setNumberOfTries();

    init();
}


/**
 * Counts the final score based on the length of hidden code
 * and number of turns/used tries.
 * @returns {number} that's the final score.
 */
function countMastermindScore() {
    return codeLength * 20 - (submitTry * tries);
}

// noinspection JSValidateTypes,JSVoidFunctionReturnValueUsed

const main_display = document.querySelector('#main');
const div_select_colors = document.getElementById('div-select-color');
const submit_button = document.getElementById('submit-btn');

let codeLength = 4;
let tries = 8;
let colors = ['blue', 'yellow', 'orange', 'green', 'violet', 'purple'];

let random_code = [];
let submitTry = 1;

let score = 0;

document.onload = init();

/**
 * The initial function that loads all the necessary stuff to the HTML file
 * and sets the dynamic parts.
 */
function init() {
    random_code = [];
    submitTry = 1;
    main_display.innerHTML = '';
    div_select_colors.innerHTML = '';
    score = codeLength * 20;

    setTries();
    setColorSelection();
    createRandomCode();
}

/**
 * Sets the columns for tries and corrections for the HTML file.
 */
function setTries() {
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
        main_display.prepend(div_try);
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
        for (let color of colors) {
            let option = document.createElement('option');
            option.setAttribute('style', 'background-color:' + color);
            option.setAttribute('value', color);
            select.append(option);
        }

        select.setAttribute('style', 'background-color:' + colors[0]);

        select.addEventListener('change', (e) => {
            e.target.setAttribute('style', 'background-color:' + e.target.value)
        });

        div_select_wrapper.append(select);
        div_select_colors.append(div_select_wrapper);
    }
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
 * Prepares the random code, the hidden colors.
 */
function createRandomCode() {
    for (let i = 1; i <= codeLength; i++) {
        let random_color = colors[Math.floor(Math.random() * colors.length)]
        random_code.push(random_color);
    }

    console.log(random_code);
}

/**
 * Adds an even listener to the submit button.
 * Submits a guess and immediately checks colors and their positions.
 */
submit_button.addEventListener('click', () => {
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
 * @param input_colors_arr is the guess, the array of colors submitted.
 * @returns {*[]} is the array of displaying which colors are correctly placed.
 */
function createCorrectionArray(input_colors_arr) {
    let random_code_copy = [...random_code];
    let correction_array = [];

    /*
    Check if color is in solution -> white
    and if color is at correct position -> red
    */
    for (let i in random_code_copy) {
        if (random_code_copy[i] === input_colors_arr[i]) {
            correction_array[i] = 'red';
        } else if (random_code_copy.includes(input_colors_arr[i])) {
            correction_array[i] = 'white';
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
        if (v === 'red') {
            countCorrect++;
        }
    }

    if (countCorrect === codeLength) {
        alert('VICTORY\nYour score is: ' + score);
        init();
    } else if (submitTry > tries) {
        alert('GAME OVER');
        init();
    } else {
        score -= tries;
    }
}

/**
 * Lets the user choose the length of hidden code
 * and the number of tries.
 */
function setAndStartNewGame() {
    setCodeLength();
    setNumberOfTries();
    score = codeLength * 20;

    init();
}
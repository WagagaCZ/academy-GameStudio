
const main_display = document.querySelector('main');
const div_select_colors = document.getElementById('div-select-color');
const submit_button = document.getElementById('submit-btn');
const restart_button = document.getElementById('restart-btn')

let codeLength = 4;
let tries = 8;
let colors = ['blue', 'yellow', 'orange', 'green', 'violet', 'purple'];

let random_code = [];
let submitTry = 1;

init();

function init() {
    random_code = [];
    submitTry = 1;
    main_display.innerHTML = '';
    div_select_colors.innerHTML = '';

    setTries();
    setCodeAndColors();
    createRandomCode();
}

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

function setCodeAndColors() {
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

function createRandomCode() {
    for (let i = 1; i <= codeLength; i++) {
        let random_color = colors[Math.floor(Math.random() * colors.length)]
        random_code.push(random_color);
    }

    console.log(random_code);
}

/*
Should load all the input colors from  div-select-color,
resp. all "selects" in it.
*
*/
submit_button.addEventListener('click', (e) => {
    let input_colors = document.querySelectorAll('.select-wrapper>select');
    let input_colors_arr = [];
    for (let v of input_colors) {
        input_colors_arr.push(v.value);
    }

    show('left', input_colors_arr);

    correction_array = createCorrectionArray(input_colors_arr);
    show('right', correction_array);

    submitTry++;
    checkWin(correction_array);
});

function show(type, colors) {
    let tryView = document.querySelectorAll('#try-' + submitTry + '>.' + type + '>div')
    tryView.forEach((v, i) => {
        v.setAttribute('style', 'background-color:' + colors[i]);
    });
}

function createCorrectionArray(input_colors_arr) {
    let random_code_copy = [...random_code];
    let correction_array = [];

    /*
    Check if color is in solution -> white
    and if color is at correct position -> red
    */
    for (let i in random_code_copy) {
        if (random_code_copy[i] == input_colors_arr[i]) {
            correction_array[i] = 'red';
        } else if (random_code_copy.includes(input_colors_arr[i])) {
            correction_array[i] = 'white';
        }
    }

    return correction_array;
}

/*
Check correction_array if all colors are correct
*/
function checkWin(correction_array) {
    let countCorrect = 0;
    for (let v of correction_array) {
        if (v == 'red') {
            countCorrect++;
        }
    }

    if (countCorrect == codeLength) {
        alert('VICTORY');
        init();
    } else if (submitTry > tries) {
        alert('GAME OVER');
        init();
    }
}

function setAndStartNewGame() {
    setCodeLength();
    setNumberOfTries();

    init();
}
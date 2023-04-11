
const SCORE_TABLE = document.querySelector("#score-table");

const COMMENT_TABLE = document.querySelector("#comment-table");
const COMMENT_FORM = document.querySelector("#comment-form");

const ratingDisplay = document.querySelector("#rating");

const RATING_BTN_1 = document.getElementById('rating-1');
const RATING_BTN_2 = document.getElementById('rating-2');
const RATING_BTN_3 = document.getElementById('rating-3');
const RATING_BTN_4 = document.getElementById('rating-4');
const RATING_BTN_5 = document.getElementById('rating-5');

// Used Stefan's services and functions loading tables with a few changes

/*
SCORE TABLE
 */
async function showScores() {
    let scores = await apiGetScores(GAME_NAME);

    if(scores.message) {
        SCORE_TABLE.innerHTML = `<tr><td></td><td></td><td style='text-align: end'>${scores.message} </td></tr>`;
        return;
    }

    // Clear table first
    SCORE_TABLE.innerHTML = '';

    // Add headers
    SCORE_TABLE.innerHTML += `
    <thead>
            <tr>
                <th>Player</th>
                <th>Score</th>
                <th>Played on</th>
            </tr>
    </thead>
    `;

    // Add score data
    scores.forEach(score => {
        let date = new Date(score.playedOn);
        date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
        SCORE_TABLE.innerHTML += `
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
        await apiSendScore(player, GAME_NAME, countMastermindScore());
        await showScores(GAME_NAME)
    } else {
        alert("You aren't logged in. Your score won't be saved.")
    }
}


/*
COMMENT TABLE
 */
async function showComments() {
    let comments = await apiGetComments(GAME_NAME);
    // clear table first
    COMMENT_TABLE.innerHTML = '';

    if(comments.message) {
        COMMENT_TABLE.innerHTML = `<tr><td>${comments.message} </td></tr>`;
        return;
    }

    COMMENT_TABLE.innerHTML += `
    <thead>
            <tr>
                <th>Player & Time</th>
                <th>Comment</th>
            </tr>
    </thead>
    `;

    comments.forEach(comment => {
        let date = new Date(comment.commentedOn);
        date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
        COMMENT_TABLE.innerHTML += `
      <tr">
          
          <td>
            <span><i class="fa-solid fa-user"></i> </span>
            <span>${comment.player}</span>
            <span> &nbsp </span>
            <span><i class="fa-solid fa-calendar-days"></i> </span>
            <span>${date}</span>
          </td>
          <td>${comment.comment}</td>
      </tr>
      `;
    });
}

async function submitComment() {
    const res = await apiGetUser();
    const player = res.loggedUser;

    const comment = COMMENT_FORM[0].value;

    if (player != null) {
        if (comment.length > 0) {

            await apiSendComment(player, GAME_NAME, comment);
            COMMENT_FORM[0].value = '';
            showComments(GAME_NAME);
        }
    } else {
        alert("You aren't logged in. Your comment won't be saved.")
    }
}

async function showAverageRating() {
    let rating = await apiGetAvgRating(GAME_NAME);

    if(rating.message) {
        ratingDisplay.innerHTML = `<h4 style="font-size: 0.8rem">${rating.message}</h4>`;
        return;
    }

    if (rating > 0) {
        let formattedRating = rating.toFixed(1);
        ratingDisplay.innerHTML = `<span>${formattedRating}</span>`;
    } else {
        ratingDisplay.innerHTML = `Game not rated yet`;
    }
}


RATING_BTN_1.addEventListener('click', () => {
    submitRating(1);
});

RATING_BTN_2.addEventListener('click', () => {
    submitRating(2);
});

RATING_BTN_3.addEventListener('click', () => {
    submitRating(3);
});

RATING_BTN_4.addEventListener('click', () => {
    submitRating(4);
});

RATING_BTN_5.addEventListener('click', () => {
    submitRating(5);
});


async function submitRating(rating) {
    const res = await apiGetUser();
    const player = res.loggedUser;

    if (player == null) {
        alert("You can't submit a rating when you aren't logged in.");
        return;
    }

    await apiSendRating(player, GAME_NAME, rating);
    showAverageRating();
}
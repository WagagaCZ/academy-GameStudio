const SCORE_TABLE = document.querySelector("#score-table");
const COMMENT_TABLE = document.querySelector("#comment-table");


// Used Stefan's services and functions loading tables with minimal changes

/*
SCORE TABLE
 */
async function showScores(game) {
    let scores = await apiGetScores(game);

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

async function sendScoreAndReloadTable(game) {
    const res = await apiGetUser();
    const player = res.loggedUser;

    if (player != null) {
        await apiSendScore(player, game, countMastermindScore());
        await showScores(game)
    } else {
        alert("You aren't logged in. Your score won't be saved.")
    }
}

/*
COMMENT TABLE
 */
async function showComments(game) {
    let comments = await apiGetComments(game);
    // clear table first
    COMMENT_TABLE.innerHTML = '';

    if(comments.message) {
        COMMENT_TABLE.innerHTML = `<tr><td>${comments.message} </td></tr>`;
        return;
    }

    comments.forEach(comment => {
        let date = new Date(comment.commentedOn);
        date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
        COMMENT_TABLE.innerHTML += `
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
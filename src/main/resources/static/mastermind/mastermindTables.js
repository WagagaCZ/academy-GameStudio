const scoreTable = document.querySelector("#score-table");

// Used Stefan's services and functions loading tables with minimal changes

async function showScores(game) {
    let scores = await apiGetScores(game);

    if(scores.message) {
        scoreTable.innerHTML = `<tr><td></td><td></td><td style='text-align: end'>${scores.message} </td></tr>`;
        return;
    }

    // clear table first
    scoreTable.innerHTML = '';

    scoreTable.innerHTML += `
    <thead>
            <tr>
                <th>Player</th>
                <th>Score</th>
                <th>Played on</th>
            </tr>
    </thead>
    `;

    scores.forEach(score => {
        let date = new Date(score.playedOn);
        date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
        scoreTable.innerHTML += `
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
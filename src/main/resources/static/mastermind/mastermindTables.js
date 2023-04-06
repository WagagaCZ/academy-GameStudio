const scoreTableBody = document.querySelector("#score-table-body");

async function showScores(game) {
    let scores = await apiGetScores(game);

    if(scores.message) {
        scoreTableBody.innerHTML = `<tr><td></td><td></td><td style='text-align: end'>${scores.message} </td></tr>`;
        return;
    }

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


async function sendScoreAndReloadTable(game) {
    const res = await apiGetUser();
    const player = res.loggedUser;

    if (player != null) {
        await apiSendScore(player, game, countScore());
        await showScores(game)
    }
}
const scoreTableBody = document.querySelector("#score-table-body");

const commentTableBody = document.querySelector("#comment-table-body");
const commentForm = document.querySelector("#comment-form");
// const commentSubmitBtn = document.querySelector("#comment-submit");

const ratingDisplay = document.querySelector("#rating");
const ratingForm = document.querySelector("#rating-select");
const ratingRadios = ratingForm.querySelectorAll("input[type=radio]");
// const ratingSubmitBtn = document.querySelector("#rating-button");


/////////////////////////
// score api

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
    showScores(game)
  }
}

//////////////////////////////
// RATING API
async function showRatings(game) {
  let rating = await apiGetAvgRating(game);

  if(rating.message) {
    ratingDisplay.innerHTML = `<p style="font-size: 0.8rem">${rating.message}</p>`;
    return;
  }

  if (rating > 0) {
    let formattedRating = rating.toFixed(2);
    ratingDisplay.innerHTML = `Rating: <span>${formattedRating}</span>`;
  } else {
    ratingDisplay.innerHTML = `Game not rated yet`;
  }
}

async function submitRating(game) {
  const res = await apiGetUser();
  const player = res.loggedUser;

  if (player == null) {
    alert("log in first");
    return;
  }

  let rating = -1;
  ratingRadios.forEach(radio => {
    if (radio.checked) {
      rating = radio.value;
    }
  });

  if (rating == -1) {
    return;
  }

  await apiSendRating(player, game, rating);
  animateRating();
  ratingRadios.forEach(radio => {
    if (radio.checked) {
      radio.checked = false;
    }
  });
  showRatings(game);
}

function animateRating() {
  ratingDisplay.classList.add("animate");
  setTimeout(() => { ratingDisplay.classList.remove("animate") }, 900);
}


// COMMENT API
async function showComments(game) {
  let comments = await apiGetComments(game);
  // clear table first
  commentTableBody.innerHTML = '';

  if(comments.message) {
    commentTableBody.innerHTML = `<tr><td>${comments.message} </td></tr>`;
    return;
  }

  comments.forEach(comment => {
    let date = new Date(comment.commentedOn);
    date = date.toLocaleDateString("en-GB") + ' ' + date.toLocaleTimeString("en-GB");
    commentTableBody.innerHTML += `
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

async function submitComment(game) {
  const player = commentForm[0].value;
  const comment = commentForm[1].value;
  if (comment.length > 0) {

    await apiSendComment(player,game, comment);
    commentForm[0].value = '';
    commentForm[1].value = '';
    showComments(game);
  }
}
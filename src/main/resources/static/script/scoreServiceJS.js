console.log("ScoreServiceJS  loaded");

const APIURL = 'http://localhost:8080/api/v2';

// POST request
const apiSendScore = async (player, game, points) => {
  try {

    const score = { player, game, points };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(score)
    };

    const response = await fetch(`${APIURL}/score`, options);
    const data = await response.json();

    console.log(data);

  } catch (err) { console.log(err) }
}

// GET request
const apiGetScores = async (game) => {
  try {

    const response = await fetch(`${APIURL}/score/top/${game}`);
    const scores = await response.json();

    return scores;

  } catch (err) { console.log(err) }
}

// GET User
const apiGetUser = async () => {
  try {

    const response = await fetch(`${APIURL}/user`);
    const scores = await response.json();

    return scores;

  } catch (err) { console.log(err) }
}
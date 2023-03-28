console.log("ScoreServiceJS  loaded");

const APIURL = 'http://localhost:8081/api';

const sendScore = async (player, game, points) => {
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

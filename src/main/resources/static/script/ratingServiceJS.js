
const RATING_API = 'http://localhost:8080/api/v2/rating';

const apiSendRating = async (player, game, ratingNumber) => {
  try {

    const rating = { username: player, game, rating: +ratingNumber };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(rating)
    };

    const response = await fetch(`${RATING_API}`, options);
    const data = await response.json();

  } catch (err) { console.log(err) }
}

// GET request
const apiGetAvgRating = async (game) => {
  try {

    const response = await fetch(`${RATING_API}/avg/${game}`);
    const rating = await response.json();

    return rating;

  } catch (err) { console.log(err) }
}
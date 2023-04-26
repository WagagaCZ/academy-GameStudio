
const COMMENT_API = 'http://localhost:8080/api/v2/comment';

const apiSendComment = async (player, game, commentText) => {
  try {
    const comment = { player, game, comment: commentText };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(comment)
    };

    const response = await fetch(`${COMMENT_API}`, options);
    const data = await response.json();

    console.log(data);
  } catch (err) { console.log(err) }
}

// GET request
const apiGetComments = async (game) => {
  try {
    const response = await fetch(`${COMMENT_API}/${game}`);
    const comments = await response.json();

    return comments;
  } catch (err) { console.log(err) }
}
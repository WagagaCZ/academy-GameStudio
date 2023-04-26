
// GET User
const apiGetUser = async () => {
  try {

    const response = await fetch(`http://localhost:8080/api/v2/user`);
    const scores = await response.json();

    return scores;
  } catch (err) { console.log(err) }
}
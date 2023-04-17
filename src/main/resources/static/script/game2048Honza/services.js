const scoresTableBody = document.getElementById("scoresTableBody");
const ratingsTableBody = document.getElementById("ratingsTableBody");
const commentsTableBody = document.getElementById("commentsTableBody");

const API_URL_SERVICE = "/api";

const getScoresUrl = "/score/2048";
const getRatingsUrl = "/rating/2048";
const getCommentsUrl = "/comment/2048";

let scores;
let comments;
let ratings;

fetchAndRenderScores();
// fetchAndRenderRatings();
// fetchAndRenderComments();

async function fetchAndRenderScores(){
    try{
        const response = await fetch(API_URL_SERVICE + getScoresUrl);
                if( response.status === 200 ) {
                    scores = await response.json();
                    renderScores(scores,scoresTableBody);
                }
    } catch ( err ) {
        console.error("Couldn't fetch scores " + err)
    }
};

async function fetchAndRenderRatings(){
    try{
        const response = await fetch(API_URL_SERVICE + getRatingsUrl);
                if( response.status === 200 ) {
                    ratings = await response.json();
                    renderRatings(ratings,ratingsTableBody);
                }
    } catch ( err ) {
        console.error("Couldn't fetch scores " + err)
    }
};
async function fetchAndRenderComments(){
    try{
        const response = await fetch(API_URL_SERVICE + getCommentsUrl);
                if( response.status === 200 ) {
                    comments = await response.json();
                    renderComments(comments,commentsTableBody);
                }
    } catch ( err ) {
        console.error("Couldn't fetch scores " + err)
    }
};


function renderScores(scoresData, renderTo){
    const rowCount = scoresData.length;
    let renderRes="";
    for(let row=0; row<rowCount;row++){
        renderRes+=
            `
                <tr>
                <td>${row+1}</td>
                <td>${scoresData[row].player}</td>
                <td>${scoresData[row].points}</td>
                <td>${scoresData[row].playedOn}</td>
                </tr>
                `
    }
    renderTo.innerHTML=renderRes;
};

function renderComments(commentsData, renderTo){
    const rowCount = commentsData.length;
    let renderRes="";
    for(let row=0; row<rowCount;row++){
        renderRes+=
            `
                <tr>
                <td>${row+1}</td>
                <td>${commentsData[row].player}</td>
                <td>${commentsData[row].comment}</td>
                <td>${commentsData[row].commentedOn}</td>
                </tr>
                `
    }
    renderTo.innerHTML=renderRes;
};

function renderRatings(ratingsData, renderTo){
    const rowCount = ratingsData.length;
    let renderRes="";
    for(let row=0; row<rowCount;row++){
        renderRes+=
            `
                <tr>
                <td>${row+1}</td>
                <td>${ratingsData[row].player}</td>
                <td>${ratingsData[row].rating}</td>
                <td>${ratingsData[row].ratedAt}</td>
                </tr>
                `
    }
    renderTo.innerHTML=renderRes;
};
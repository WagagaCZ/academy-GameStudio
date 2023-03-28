console.log("Timer JS loaded");

const timerDisplay = document.querySelector(".timer-display");
// const timerStart = document.querySelector(".timer-start");
// const timerPause = document.querySelector(".timer-pause");
// const timerReset = document.querySelector(".timer-reset");

let ml = 0;
let display = "00m:00s:000ms"
let lastUpdated = performance.now()

let timerInterval = null;

timerDisplay.innerHTML = display;

// timerStart.addEventListener("click", startTimer);
// timerPause.addEventListener("click", pauseTimer);
// timerReset.addEventListener("click", resetTimer);

function startTimer() {
  clearInterval(timerInterval)
  lastUpdated = performance.now()
  timerInterval = setInterval(() => {
    updateTimer()
  }, 10)
}

function pauseTimer() {
  clearInterval(timerInterval)
}

function resetTimer() {
  ml = 0
  clearInterval(timerInterval)
  lastUpdated = performance.now()
  updateTimer()
}

function updateTimer() {
  let now = performance.now()
  let time = now - lastUpdated
  lastUpdated = now;
  ml += time
  let minutes = Math.floor(ml / 60000).toString().padStart(2, "0")
  let seconds = Math.floor((ml % 60000) / 1000).toString().padStart(2, "0")
  let mili = (ml % 1000).toString().padStart(3, "0")
  timerDisplay.innerHTML = (minutes + "m:") + (seconds + "s:") + (mili + "ms")
}

function getTime() {
  return ml
}
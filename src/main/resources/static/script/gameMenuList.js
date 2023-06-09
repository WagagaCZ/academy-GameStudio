const gamesMenu = document.querySelector(".games-menu");

const games = [
  {
    name: "Minesweeper",
    url: "/mines",
    creator: "Matus",
    icon: "fa-solid fa-bomb",
  },
  {
  name: "Minesweeper asynch",
  url: "/mines/asynch",
  creator: "Matus",
  icon: "fa-solid fa-land-mine-on",
  },
  {
    name: "TIC-TAC-TOE",
    url: "/toe",
    creator: "Anicka",
    icon: "fa-solid fa-times",
  },
  {
    name: "MinesweeperJS",
    url: "/minesweeperJS",
    creator: "stevo",
    icon: "fa-solid fa-flag",
  },
  {
    name: "Game1024JS",
    url: "/game1024",
    creator: "stevo",
    icon: "fa-solid fa-hashtag",
  },
  {
    name: "ConnectFour",
    url: "/connect",
    creator: "Mark",
    icon: "fa-solid fa-circle",
  },
  {
    name: "BlackJack",
    url: "/blackjack",
    creator: "Matej",
    icon: "fa-solid fa-diamond",
  },
  {
    name: "Battleship",
    url: "/ship",
    creator: "Riso",
    icon: "fa-solid fa-ship",
  },
  {
    name: "Candy Crush",
    url: "/candycrush",
    creator: "Dima",
    icon: "fa-solid fa-candy",
  },
  {
    name: "Mastermind",
    url: "/mastermind",
    creator: "Mila",
    icon: "fa-solid fa-brain",
  },   
  {
    name: "PUZZLE FIFTEEN",
    url: "/puzzle",
    creator: "Anicka",
    icon: "fa-solid fa-puzzle-piece",
  },
  {
    name: "Pexeso",
    url: "/pexeso",
    creator: "Maros",
    icon: "fa-solid fa-magnifying-glass",
  },
  {
      name: "Poker",
      url: "/poker",
      creator: "Martin",
      icon: "fa-solid fa-heart",
   },
   {
      name: "2048",
      url: "/2048",
      creator: "Honza",
      icon: "fa-solid fa-grid",
    },
];



function createList() {
  gamesMenu.innerHTML = "";

  games.forEach((game) => {

    const gameItem = document.createElement("div");
    gameItem.innerHTML = `
      <a href="${game.url}" class="game-link">
        <div class="game-name">${game.name}</div>
        <div class="game-icon"><i class="${game.icon}"></i></div>
        <div class="game-creator"><i class="fa-regular fa-user"></i> ${game.creator}</div>
      </a>
    `;
    gamesMenu.append(gameItem);
  });
}

createList();

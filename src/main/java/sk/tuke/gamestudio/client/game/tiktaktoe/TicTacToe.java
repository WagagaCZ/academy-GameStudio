package sk.tuke.gamestudio.client.game.tiktaktoe;

public class TicTacToe {
    public TicTacToe() {
        TicTacConsoleUI ticTacConsoleUI = new TicTacConsoleUI();
        ticTacConsoleUI.play();
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}



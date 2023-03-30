package sk.tuke.gamestudio.client.game.mastermind;

import sk.tuke.gamestudio.client.game.mastermind.consoleui.MastermindConsoleUI;
import sk.tuke.gamestudio.client.game.mastermind.core.MastermindField;

public class Mastermind {

    private static MastermindConsoleUI mastermindConsoleUI;

    public static void main(String[] args) {
        play();
    }

    public static void play() {
        mastermindConsoleUI = new MastermindConsoleUI();
        MastermindField field = new MastermindField(10);
        mastermindConsoleUI.newGameStarted(field);
    }

    public String getName() {
        return "Mastermind";
    }
}

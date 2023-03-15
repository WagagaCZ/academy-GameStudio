package sk.tuke.gamestudio.client.game.minesweeper;

import sk.tuke.gamestudio.client.game.minesweeper.consoleui.ConsoleUI;
import sk.tuke.gamestudio.client.game.minesweeper.core.Field;

/**
 * Main application class.
 */
public class Minesweeper {
    /** User interface. */
    private ConsoleUI userInterface;
 
    /**
     * Constructor.
     */
    private Minesweeper() {
        userInterface = new ConsoleUI();
        
        Field field = new Field(10, 10, 1);
        userInterface.newGameStarted(field);
    }

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}

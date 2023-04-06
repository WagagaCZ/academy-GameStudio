package sk.tuke.gamestudio.client.game.game2048;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.client.game.game2048.consoleui.ConsoleUI;
import sk.tuke.gamestudio.client.game.game2048.core.Field;

@Component
public class Game1024 {
    @Autowired
    @Qualifier("2048_console")
    private ConsoleUI userInterface;

    /**
     * Creates new field and starts console UI
     */
    public void run() {
        Field field = new Field(4 ,4);
        userInterface.newGameStarted(field);
    }
    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        System.out.println( String.format( "Player : %s" , System.getProperty("user.name")));
        new Game1024();
    }
}

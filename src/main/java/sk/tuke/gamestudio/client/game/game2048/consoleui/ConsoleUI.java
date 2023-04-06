package sk.tuke.gamestudio.client.game.game2048.consoleui;

import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.client.game.game2048.core.Direction;
import sk.tuke.gamestudio.client.game.game2048.core.Field;
import sk.tuke.gamestudio.client.game.game2048.core.GameState;
import sk.tuke.gamestudio.client.game.game2048.exceptions.GameEndException;
import sk.tuke.gamestudio.client.game.game2048.exceptions.UnknownCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("2048_console")
public class ConsoleUI {
    /** Playing field. */
    private Field field;
    /** Input reader. */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Pattern inputPattern = Pattern.compile("^(w|up|s|down|a|left|d|right)$");
    /**
     * Reads line of text from the reader.
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     * @param field field of mines and clues
     */
    public void newGameStarted(Field field) {
        this.field = field;
        boolean gameEnd = false;
        do {
            update();
            try {
                processInput();
            } catch( GameEndException e ) {
                return;
            }

            GameState fieldState = field.getState();

            if (fieldState == GameState.FAILED) {
                System.out.println("YOU LOSE!!!");
                gameEnd = true;
            }
            if (fieldState == GameState.SOLVED) {
                System.out.println("YOU WON!");
                gameEnd = true;
            }
        } while( !gameEnd );
    }
    /**
     * Updates user interface - prints the field.
     */
    public void update() {
        System.out.println( field.toString() );
        System.out.println("\"Write input: ?( move : wasd | exit : \"exit\" )");
    }

    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() throws GameEndException {
        String input = this.readLine();
        if( input == null )
            throw new IllegalArgumentException("Argument is null!");
        if( "exit".equalsIgnoreCase( input ) )
            throw new GameEndException("game close");

        try {
            handleInput( input );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }

    }
    private Direction inputToDirection(String input ) {
        if( "w".equals(input) || "up".equals(input) )
            return Direction.UP;
        else if( "a".equals(input) || "left".equals(input) )
            return Direction.LEFT;
        else if( "s".equals(input) || "down".equals(input) )
            return Direction.DOWN;
        else
            return Direction.RIGHT;
    }
    private void handleInput( String input ) throws UnknownCommandException, IndexOutOfBoundsException {
        Matcher matcher = inputPattern.matcher( input );
        if( !matcher.matches() )
            throw new UnknownCommandException("Unknown command (\"" + input + "\")" );
        field.doMove( inputToDirection( input ) );
    }
}

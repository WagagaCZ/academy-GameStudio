package sk.tuke.gamestudio.game.minesweeper.consoleui;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.regex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.minesweeper.core.Field;
import sk.tuke.gamestudio.game.minesweeper.core.GameState;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

/**
 * Console user interface.
 */
@Component
public class ConsoleUI implements UserInterface {
    /** Playing field. */
    private Field field;

    /** Regular expression pattern for the user input */
    Pattern OPEN_MARK_PATTERN = Pattern.compile("([OM]{1})([A-Z]{1})([0-9]{1,})");
    
    /** Input reader. */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Autowired
    private ScoreService scoreService;
    
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
    @Override
    public void newGameStarted(Field field) {
        this.field = field;
        var gameShouldContinue = true;
        do {
            update();
            processInput();

            var fieldState = field.getState();

            if (fieldState == GameState.FAILED) {
                System.out.println("You uncovered a mine. You lost.");
                gameShouldContinue = false;
            }
            if (fieldState == GameState.SOLVED) {
                System.out.println("You won, congratulations.");

                var score = field.getScore();
                System.out.println("Your score is: " + score);

                scoreService.addScore(new Score(
                        "mines",
                        System.getProperty("user.name"),
                        score,
                        new Date()
                ));

                printScores();

                gameShouldContinue = false;
            }
        } while(gameShouldContinue);

        System.exit(0);
    }
    
    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        var columns = field.getColumnCount();
        var length = (int) (Math.log10(columns) + 1) + 1;
        var format = "%" + length + "s";

        System.out.printf(format, "");
        for (int c = 0; c < columns; c++) {
            System.out.printf(format, c + 1);
        }
        System.out.println();
        for (int r = 0; r < field.getRowCount(); r++) {
            System.out.printf(format, (char)(r+65));
            for (int c = 0; c < columns; c++) {
                System.out.printf(format, field.getTile(r, c));
            }
            System.out.println();
        }
    }

    private void processInput() {
        System.out.println("Enter your selection.");
        System.out.println("""
        Expected output: X - end game, M - mark, O - open, S - list best scores, C - all comments, R - game rating
        Example: MA1 - marking a tile in row A and column 1""");

        String playerInput = readLine();
        if(playerInput == null) {
            System.out.println("Nespravny vstup");
            processInput();
            return;
        }
        playerInput = playerInput.trim().toUpperCase();

        if(playerInput.equals("X")) {
            System.out.println("Closing the game.");
            System.exit(0);
        }

        try {
            handleServices(playerInput);
            return;
        } catch (WrongFormatException e) {
            //empty on purpose
        }

        // overi format vstupu - exception handling
        try {
            handleInput(playerInput);
        } catch (WrongFormatException e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
            System.out.println("Please repeat your entry.");
            processInput();
        }
    }

    private void handleServices(String playerInput) throws WrongFormatException {
        switch (playerInput) {
            case "S" -> printScores();
            case "C", "R" -> System.out.println("Operation not yet supported.");
            default -> throw new WrongFormatException("Service unknown.");
        }
    }

    private void handleInput(String playerInput) throws WrongFormatException {
        Matcher matcher = OPEN_MARK_PATTERN.matcher(playerInput);

        if (!OPEN_MARK_PATTERN.matcher(playerInput).matches()) {
            throw new WrongFormatException("Incorrect input format entered.");
        }

        //noinspection ResultOfMethodCallIgnored
        matcher.find(); //to create groups

        int row = matcher.group(2).charAt(0) - 65,
            col = Integer.parseInt(matcher.group(3)) - 1;

        if (!isInputInBorderOfField(row, col)) {
            processInput();
            return;
        }

        doOperation(matcher.group(1).charAt(0), row, col);
    }

    private void doOperation(char operation, int row, int col) {
        if (operation == 'M') {
            field.markTile(row, col);
        }

        if (operation == 'O') {
            field.openTile(row, col);
        }
    }

    private boolean isInputInBorderOfField(int row, int col) {
        if (row >= field.getRowCount()) {
            System.err.println("Letter is larger than the number of rows.");
            return false;
        }
        if (col >= field.getColumnCount()) {
            System.err.println("Number is larger than the number of columns.");
            return false;
        }
        return true;
    }

    private void printScores() {
        try {
            List<Score> scores = scoreService.getTopScores("mines");
            System.out.println("MINES - TOP SCORES:");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("%10s\t%10s\t%5s\t%s%n", "Game", "Player", "Points", "Date");
            scores.forEach(System.out::println);
            System.out.println("--------------------------------------------------------------");
        } catch(ScoreException e) {
            System.out.println("Problem getting scores from the database.");
        }
    }
}

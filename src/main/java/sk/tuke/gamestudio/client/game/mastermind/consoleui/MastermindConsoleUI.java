package sk.tuke.gamestudio.client.game.mastermind.consoleui;

import sk.tuke.gamestudio.client.game.mastermind.core.GameState;
import sk.tuke.gamestudio.client.game.mastermind.core.MastermindField;
import sk.tuke.gamestudio.client.game.mastermind.core.Peg;
import sk.tuke.gamestudio.client.game.mastermind.core.PegColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

public class MastermindConsoleUI {

    private final String GAME_NAME = "Mastermind";

    private MastermindField field;

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


    public void newGameStarted(MastermindField field) {
        this.field = field;
        boolean playing = true;

        do {
            newTurn();

            if (Objects.requireNonNull(field.getState()) == GameState.SOLVED) {
                winGame();
                playing = false;
            } else if (field.getState() == GameState.FAILED) {
                loseGame();
                playing = false;
            }
        } while (playing);
    }

    private void winGame() {

    }

    private void loseGame() {

    }

    private void newTurn() {
        field.decreaseTriesLeft();
        update();
        processInput();
    }

    public void update() {
        System.out.println("  " + getVerticalBorder());
        for (int r = 0; r < field.getHeight(); r++) {
            if (field.getTriesLeft() == r) {
                System.out.println("> ");
            } else {
                System.out.println("  ");
            }

            for (int c = 0; c < 5; c++) {
                if (field.getPeg(r, c) == null) {
                    System.out.println("        ");
                } else {
                    System.out.println(field.getPeg(r, c) + " ");
                }
            }

            printClue(r, field.getPeg(r, 0));

            System.out.println("\n  " + getVerticalBorder());
        }
    }


    private void printClue(int row, Peg firstPeg) {
        if (row != 0 && firstPeg != null) {
            System.out.println("| ");
            for (int pegIndex = 0; pegIndex < 5; pegIndex++) {
                PegColor actualPegVal = field.getPeg(row, pegIndex).getColor();
                PegColor firstRowPegVal = field.getPeg(0, pegIndex).getColor();
                if (actualPegVal == firstRowPegVal) {
                    System.out.print("* ");
                } else if (field.presentInHiddenPegs(field.getPeg(row, pegIndex))) {
                    System.out.print("! ");
                } else {
                    System.out.print("X ");
                }
            }
        }
    }

    public void processInput() {
        String input = readLine();
        field.setGuessPegs(input.toCharArray());
    }

    public String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private String getVerticalBorder() {
        String[] verticalBorder = new String[29];
        Arrays.fill(verticalBorder, "-");
        return String.join("", verticalBorder);
    }
}

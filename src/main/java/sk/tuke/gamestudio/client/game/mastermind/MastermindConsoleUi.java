package sk.tuke.gamestudio.client.game.mastermind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * Not completed
 */

public class MastermindConsoleUi {

    private final String GAME_NAME = "Mastermind";

    private MastermindField field;

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


    public void update() {
        for (int guessIndex = 0; guessIndex < field.getHeight(); guessIndex++) {
            StringBuilder actualRow = new StringBuilder();

            actualRow.append("  ");

            for (int pegIndex = 0; pegIndex < field.getCodeLength(); pegIndex++) {
                String pegColor = field.getPegColor(guessIndex, pegIndex);
                if (pegColor != null) {
                    int colorIndex = Arrays.asList(field.getCOLORS()).indexOf(pegColor);
                    actualRow.append("( ").append(colorIndex).append(" ) ");
                } else {
                    actualRow.append("(   ) ");
                }
            }

            for (int correctionIndex = 0; correctionIndex < field.getCodeLength(); correctionIndex++) {
                String verdict = field.getCorrection(guessIndex, correctionIndex);
                if (verdict != null) {
                    actualRow.append("[" + verdict + "] ");
                } else {
                    actualRow.append("[  " + "  ] ");
                }
            }

            actualRow.append("\n");
        }
    }


    public String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}

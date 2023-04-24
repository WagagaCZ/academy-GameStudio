package sk.tuke.gamestudio.client.game.mastermind;

import java.util.Random;

public class MastermindField {

    private final String[] COLORS = {"blue", "yellow", "orange", "green", "violet", "purple"};
    //ja by som si toto vytvorila ako pole hodnot enumov, nie ako pole Stringov :)

    private String[][] tryField;
    private String[][] correctionField;

    private int codeLength = 4;
    private int tries = 8;
    private String[] randomCode;
    private int submitTry = 1;

    private GameState state = GameState.PLAYING;


    public MastermindField() {
        this.randomCode = new String[codeLength];

        createRandomCode();
        generate();
    }

    public MastermindField(int codeLength, int tries) {
        this.codeLength = codeLength;
        this.tries = tries;

        createRandomCode();
        generate();
    }

    private void generate() {
        tryField = new String[tries][codeLength];
        correctionField = new String[tries][codeLength];
    }

    public void guessCode(String[] colors) {
        int guessIndex = tries - submitTry;
        System.arraycopy(
                colors, 0,
                tryField[guessIndex], 0, tryField[guessIndex].length
        );

        checkGuess(guessIndex);
    }

    private void checkGuess(int guessIndex) {
        int correctColors = 0;
        boolean[] correctionArray = new boolean[codeLength];

        for (int i = 0; i < codeLength; i++) {
            if (tryField[guessIndex][i].equals(randomCode[i])) {
                correctionArray[i] = true;
                correctColors++;
            }
        }

        for (int i = 0; i < codeLength; i++) {
            correctionField[guessIndex][i] = String.valueOf(correctionArray[i]);
        }

        if (correctColors == codeLength) {
            state = GameState.SOLVED;
        } else if (guessIndex == 0) {
            state = GameState.FAILED;
        }
    }

    private void createRandomCode() {
        for (int i = 0; i < codeLength; i++) {
            randomCode[i] = getRandomColor();
        }
    }

    private String getRandomColor() {
        int rnd = new Random().nextInt(COLORS.length);
        return COLORS[rnd];
    }

    public String[] getCOLORS() {
        return COLORS;
    }

    public String[][] getTryField() {
        return tryField;
    }

    public String[][] getCorrectionField() {
        return correctionField;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public GameState getState() {
        return state;
    }

    public int getHeight() {
        return tryField.length;
    }

    public String getPegColor(int guessIndex, int pegIndex) {
        return tryField[guessIndex][pegIndex];
    }

    public String getCorrection(int guessIndex, int correctionIndex) {
        return correctionField[guessIndex][correctionIndex];
    }
}

package sk.tuke.gamestudio.client.game.mastermind.core;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MastermindField {

    private final Peg[][] pegSlots;
    private final int numberOfColors;
    private final int numberOfTries;
    private int triesLeft;
    private GameState state = GameState.PLAYING;
    private PegColor colorGenerator;

    private final long startTimeInMs;
    private int score;

    public MastermindField(int numberOfTries) {
        this.numberOfTries = numberOfTries;
        this.numberOfColors = 5;
        this.pegSlots = new Peg[numberOfTries + 1][numberOfColors];
        this.triesLeft = pegSlots.length;
        this.colorGenerator = PegColor.BLACK;

        generate();

        startTimeInMs = System.currentTimeMillis();
    }

    private void generate() {
        for (int i = 0; i < 5; i++) {
            pegSlots[0][i] = new Peg(colorGenerator.getRandomColor(), true);
        }
    }

    public void setGuessPegs(char[] input) {
        for (int pegIndex = 0; pegIndex < 5; pegIndex++) {
            pegSlots[triesLeft][pegIndex] = new Peg(
                    PegColor.values()[Character.getNumericValue(input[pegIndex])]
            );
        }

        if (isSolved()) {
            state = GameState.SOLVED;
            score = computeScore();
        } else if (triesLeft == 0) {
            state = GameState.FAILED;
        }
    }

    public boolean isSolved() {
        int rightGuesses = 0;
        for (int i = 0; i < 5; i++) {
            if (pegSlots[0][i].getColor() == pegSlots[triesLeft][i].getColor()) {
                rightGuesses++;
            }
        }

        return rightGuesses == 5;
    }

    private int computeScore() {
        int actualScore = 0;
        if (state == GameState.SOLVED) {
            actualScore = numberOfColors * numberOfColors * 10 -
                    (int) ((System.currentTimeMillis() - startTimeInMs) / 1000);
            if (score < 0) score = 0;
        }

        return actualScore;
    }

    public boolean presentInHiddenPegs(Peg peg) {
        List<PegColor> listOfColorsOfHiddenPegs = Arrays.stream(pegSlots[0])
                .map(Peg::getColor)
                .toList();

        return listOfColorsOfHiddenPegs.contains(peg.getColor());
    }

    public void decreaseTriesLeft() {
        this.triesLeft--;
    }

    public int getHeight() {
        return numberOfTries + 1;
    }

    public Peg getPeg(int row, int column) {
        return pegSlots[row][column];
    }

    public int getNumberOfColors() {
        return numberOfColors;
    }

    public int getNumberOfTries() {
        return numberOfTries;
    }

    public int getTriesLeft() {
        return triesLeft;
    }

    public GameState getState() {
        return state;
    }

    public PegColor getColorGenerator() {
        return colorGenerator;
    }

    public long getStartTimeInMs() {
        return startTimeInMs;
    }

    public int getScore() {
        return score;
    }
}

package sk.tuke.gamestudio.client.game.puzzle;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static sk.tuke.gamestudio.client.game.puzzle.PuzzleFieldInit.baseArrayInit;
import static sk.tuke.gamestudio.client.game.puzzle.PuzzleFieldInit.switchRandomTiles;

class PuzzleFieldInitTest {
    static final int rows = 4;
    static final int col = 4;
    static final int[][] puzzleField = new int[rows][col];
    static final int sumOfAllElements = 120;

    @Test
    void baseArrayInitTest() {
        int sum = 0;
        assertEquals(4, puzzleField.length);
        baseArrayInit(puzzleField);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = sum + puzzleField[i][j];
            }
        }
        assertEquals(sumOfAllElements, sum);
    }

    @Test
    void initRandomTilesTest() {
        switchRandomTiles(puzzleField);
        int[][] wonGrid = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
        assertFalse(Arrays.deepEquals(wonGrid, puzzleField));
    }

}
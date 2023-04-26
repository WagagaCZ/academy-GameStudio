package sk.tuke.gamestudio.client.game.puzzle;

import java.util.Random;

public interface PuzzleFieldInit {
    static void baseArrayInit(int[][] grid) {
        int i = 0;
        for (int row = 0; row <= grid.length - 1; row++) {
            for (int col = 0; col <= grid[0].length - 1; col++) {
                grid[row][col] = i;
                i++;
            }
        }
    }

    static void switchRandomTiles(int[][] grid) {
        int rndRowFrom, rndRowDest, rndColDest, rndColFrom;
        Random random = new Random();

        for (int replacementsNumber = 0; replacementsNumber < 15; replacementsNumber++) {
            rndRowFrom = random.nextInt(grid.length);
            rndColFrom = random.nextInt(grid.length);
            rndRowDest = random.nextInt(grid.length);
            rndColDest = random.nextInt(grid.length);

            int temp = grid[rndRowFrom][rndColFrom];
            grid[rndRowFrom][rndColFrom] = grid[rndRowDest][rndColDest];
            grid[rndRowDest][rndColDest] = temp;
        }
    }

    //if game is solvable - with algoritm
    static boolean isSolved(int[][] grid) {
        int count = 0;
        for (int row = 1; row <= grid.length - 1; row++) {
            for (int col = 1; col <= grid[0].length - 1; col++) {
                if (grid[col][row] > grid[col][row - 1]) {
                    count++;
                }
            }
        }
        return count % 2 == 0;
    }
}

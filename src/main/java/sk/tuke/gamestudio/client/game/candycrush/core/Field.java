package sk.tuke.gamestudio.client.game.candycrush.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Field {
    public Tile[][] tiles;
    public int numRows;
    public int numCols;
    public int numSuccessfulSwaps = 0;


    private GameState state = GameState.PLAYING;
    public int score;

    public final long startTimeInMs;


    public boolean justFinished=false;

    public Field(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.tiles = new Tile[numRows][numCols];
        fill();
        generate();
        startTimeInMs = System.currentTimeMillis();
    }
    private int computeScore() {
        int score = 0;
        if (state == GameState.SOLVED) {
            // Calculate score based on time elapsed and size of the field
            score = numRows * numCols * 10 - (int) ((System.currentTimeMillis() - startTimeInMs) / 1000);
            if (score < 0) {
                score = 0;
            }
        }
        return score;
    }

    public void fill() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public void display() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Tile tile = tiles[i][j];
                switch (tile.getColor()) {
                    case RED:
                        System.out.printf("\u001B[31m%s\u001B[0m ", "\uD83D\uDD25"); // Red fire emoji
                        break;
                    case BLUE:
                        System.out.printf("\u001B[34m%s\u001B[0m ", "\uD83D\uDD35"); // Blue swirl emoji
                        break;
                    case GREEN:
                        System.out.printf("\u001B[32m%s\u001B[0m ", "\uD83D\uDFE9"); // Green puzzle piece emoji
                        break;
                    case YELLOW:
                        System.out.printf("\u001B[33m%s\u001B[0m ", "\uD83D\uDFE1"); // Yellow shopping bag emoji
                        break;
                    case PURPLE:
                        System.out.printf("\u001B[35m%s\u001B[0m ", "\uD83D\uDC9C"); // Purple heart emoji
                        break;
                    default:
                        System.out.printf("\u001B[30m%s\u001B[0m ", "\uD83D\uDD34"); // Black four-leaf clover emoji
                }
            }
            System.out.println();
        }
    }




    public boolean isValidSwap(int row1, int col1, int row2, int col2) {
        if (row1 < 0 || row1 >= numRows || col1 < 0 || col1 >= numCols ||
                row2 < 0 || row2 >= numRows || col2 < 0 || col2 >= numCols) {
            return false;
        }
        if (Math.abs(row1 - row2) + Math.abs(col1 - col2) != 1) {
            return false;
        }
        return true;
    }

    public void swap(int row1, int col1, int row2, int col2) {
        Tile temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    public boolean hasThreeInARow() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols - 2; j++) {
                if (tiles[i][j].getColor() == tiles[i][j + 1].getColor() &&
                        tiles[i][j].getColor() == tiles[i][j + 2].getColor()) {
                    return true;
                }
            }
        }
        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < numRows - 2; i++) {
                if (tiles[i][j].getColor() == tiles[i + 1][j].getColor() &&
                        tiles[i][j].getColor() == tiles[i + 2][j].getColor()) {
                    return true;
                }
            }
        }
        return false;
    }
    public void removeMatches() {
        ArrayList<Tile> matchedTiles = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols - 2; j++) {
                if (tiles[i][j].getColor() == tiles[i][j+1].getColor() &&
                        tiles[i][j].getColor() == tiles[i][j+2].getColor()) {
                    matchedTiles.add(tiles[i][j]);
                    matchedTiles.add(tiles[i][j+1]);
                    matchedTiles.add(tiles[i][j+2]);
                }
            }
        }
        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < numRows - 2; i++) {
                if (tiles[i][j].getColor() == tiles[i+1][j].getColor() &&
                        tiles[i][j].getColor() == tiles[i+2][j].getColor()) {
                    matchedTiles.add(tiles[i][j]);
                    matchedTiles.add(tiles[i+1][j]);
                    matchedTiles.add(tiles[i+2][j]);
                }
            }
        }
        for (Tile tile : matchedTiles) {
            tile.setColor(TileColor.EMPTY);
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (tiles[i][j].getColor() == TileColor.EMPTY) {
                    tiles[i][j].setColor(TileColor.values()[new Random().nextInt(TileColor.values().length - 1) + 1]);
                    break;
                }
            }
        }
    }
    public boolean isSolved() {
        // Check if there are no more valid swaps left
        if (!hasValidSwap()) {
            return true;
        }

        // Check if the number of successful swaps is at least 10
        if(getNumSuccessfulSwaps() >= 10) {
            score = computeScore();
            return true;
        }
        return false;
    }

    private boolean hasValidSwap() {
        // Iterate over each tile in the field
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Check if swapping with any of the adjacent tiles results in a match
                if (col < numCols - 1 && isValidSwap(row, col, row, col + 1)) {
                    return true;
                }
                if (row < numRows - 1 && isValidSwap(row, col, row + 1, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    private int getNumSuccessfulSwaps() {
        return numSuccessfulSwaps;
    }

    public void shiftDown() {
        for (int j = 0; j < numCols; j++) {
            int emptyRow = numRows - 1;
            for (int i = numRows - 1; i >= 0; i--) {
                if (tiles[i][j].getColor() == TileColor.EMPTY) {
                    emptyRow = i;
                    break;
                }
            }
            for (int i = emptyRow - 1; i >= 0; i--) {
                if (tiles[i][j].getColor() != TileColor.EMPTY) {
                    tiles[emptyRow][j] = tiles[i][j];
                    tiles[i][j] = new Tile();
                    emptyRow--;
                }
            }
        }
    }

    public boolean isJustFinished() {
        return justFinished;
    }

    public void setJustFinished(boolean justFinished) {
        this.justFinished = justFinished;
    }
    public void generate() {
        Random random = new Random();
        TileColor[] colors = TileColor.values();
        int numColors = colors.length;

        // Check for empty tiles in last two rows and columns
        boolean emptyTilesExist = false;
        for (int i = numRows - 2; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (tiles[i][j] == null) {
                    emptyTilesExist = true;
                    break;
                }
            }
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = numCols - 2; j < numCols; j++) {
                if (tiles[i][j] == null) {
                    emptyTilesExist = true;
                    break;
                }
            }
        }

        // Generate new tiles
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                TileColor color;
                if (emptyTilesExist && (i == numRows - 1 || j == numCols - 1) && tiles[i][j] == null) {
                    // Assign a random color that is not the same as the color of adjacent tiles
                    TileColor adjacentColor = null;
                    if (i > 0 && tiles[i - 1][j] != null) {
                        adjacentColor = tiles[i - 1][j].getColor();
                    }
                    if (j > 0 && tiles[i][j - 1] != null) {
                        adjacentColor = tiles[i][j - 1].getColor();
                    }
                    do {
                        color = colors[random.nextInt(numColors)];
                    } while (color == adjacentColor);
                } else {
                    do {
                        color = colors[random.nextInt(numColors)];
                    } while (hasMatchesAt(i, j, color));
                }
                tiles[i][j] = new Tile(color);
            }
        }
        checkAndFillEmptyTiles();
    }

    private boolean hasMatchesAt(int row, int col, TileColor color) {
        // Check horizontal matches
        int numConsecutive = 1;
        for (int j = col - 1; j >= Math.max(0, col - 2) && tiles[row][j].getColor() == color; j--) {
            numConsecutive++;
        }
        for (int j = col + 1; j <= Math.min(numCols - 1, col + 2) && tiles[row][j].getColor() == color; j++) {
            numConsecutive++;
        }
        if (numConsecutive >= 3) {
            return true;
        }

        // Check vertical matches
        numConsecutive = 1;
        for (int i = row - 1; i >= Math.max(0, row - 2) && tiles[i][col].getColor() == color; i--) {
            numConsecutive++;
        }
        for (int i = row + 1; i <= Math.min(numRows - 1, row + 2) && tiles[i][col].getColor() == color; i++) {
            numConsecutive++;
        }
        if (numConsecutive >= 3) {
            return true;
        }

        // No matches found
        return false;
    }


    private void checkAndFillEmptyTiles() {
        TileColor[] colors = TileColor.values();
        Set<TileColor> invalidColors = new HashSet<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (i == 8 && j == 8 || i == 8 && j == 9 || i == 9 && j == 8 || i == 9 && j == 9) {
                    if (tiles[i][j] == null) {
                        // Fill the empty tile with a color that won't trigger a match-3
                        TileColor color;
                        do {
                            color = colors[new Random().nextInt(colors.length)];
                        } while (invalidColors.contains(color));
                        invalidColors.add(color);
                        tiles[i][j] = new Tile(color);
                    }
                }
            }
        }
    }






}

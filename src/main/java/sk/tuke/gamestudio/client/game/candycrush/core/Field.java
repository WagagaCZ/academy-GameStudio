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

    public void display() { //toto by mal riesit ConsoleUI, nie Field :)
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                Tile tile = tiles[r][c];
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
        return !(row1 < 0 || row1 >= numRows || col1 < 0 || col1 >= numCols
            || row2 < 0 || row2 >= numRows || col2 < 0 || col2 >= numCols
            || Math.abs(row1 - row2) + Math.abs(col1 - col2) != 1);
    }

    public void swap(int row1, int col1, int row2, int col2) {
        Tile temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    private boolean isMatchingTile(Tile tile1, Tile tile2, Tile tile3) {
        return tile1.getColor() == tile2.getColor() && tile1.getColor() == tile3.getColor();
    }

    public boolean hasThreeInARow() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols - 2; c++) {
                if (isMatchingTile(tiles[r][c], tiles[r][c+1], tiles[r][c+2])) {
                    return true;
                }
            }
        }
        for (int c = 0; c < numCols; c++) {
            for (int r = 0; r < numRows - 2; r++) {
                if (isMatchingTile(tiles[c][r], tiles[c+1][r], tiles[c+2][r])) {
                    return true;
                }
            }
        }
        return false;
    }
    public void removeMatches() {
        //get matching tiles
        ArrayList<Tile> matchedTiles = new ArrayList<>();
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols - 2; c++) {
                if (tiles[r][c].getColor() == tiles[r][c+1].getColor() &&
                        tiles[r][c].getColor() == tiles[r][c+2].getColor()) {
                    matchedTiles.add(tiles[r][c]);
                    matchedTiles.add(tiles[r][c+1]);
                    matchedTiles.add(tiles[r][c+2]);
                }
            }
        }
        for (int c = 0; c < numCols; c++) {
            for (int r = 0; r < numRows - 2; r++) {
                if (tiles[r][c].getColor() == tiles[r+1][c].getColor() &&
                        tiles[r][c].getColor() == tiles[r+2][c].getColor()) {
                    matchedTiles.add(tiles[r][c]);
                    matchedTiles.add(tiles[r+1][c]);
                    matchedTiles.add(tiles[r+2][c]);
                }
            }
        }

        //set them to empty
        for (Tile tile : matchedTiles) {
            tile.setColor(TileColor.EMPTY);
        }

        //remove them from the field
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (tiles[r][c].getColor() == TileColor.EMPTY) {
                    tiles[r][c].setColor(TileColor.values()[new Random().nextInt(TileColor.values().length - 1) + 1]);
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
                if (col < numCols - 1 && isValidSwap(row, col, row, col + 1)
                 || row < numRows - 1 && isValidSwap(row, col, row + 1, col)) {
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
        for (int c = 0; c < numCols; c++) {
            int emptyRow = numRows - 1;
            for (int r = numRows - 1; r >= 0; r--) {
                if (tiles[r][c].getColor() == TileColor.EMPTY) {
                    emptyRow = r;
                    break;
                }
            }
            for (int r = emptyRow - 1; r >= 0; r--) {
                if (tiles[r][c].getColor() != TileColor.EMPTY) {
                    tiles[emptyRow][c] = tiles[r][c];
                    tiles[r][c] = new Tile();
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
        for (int r = numRows - 2; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (tiles[r][c] == null) {
                    emptyTilesExist = true;
                    break;
                }
            }
        }
        for (int r = 0; r < numRows; r++) {
            for (int c = numCols - 2; c < numCols; c++) {
                if (tiles[r][c] == null) {
                    emptyTilesExist = true;
                    break;
                }
            }
        }

        // Generate new tiles
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                TileColor color;
                if (emptyTilesExist && (r == numRows - 1 || c == numCols - 1) && tiles[r][c] == null) {
                    // Assign a random color that is not the same as the color of adjacent tiles
                    TileColor adjacentColor = TileColor.EMPTY;
                    if (r > 0 && tiles[r - 1][c] != null) {
                        adjacentColor = tiles[r - 1][c].getColor();
                    }
                    if (c > 0 && tiles[r][c - 1] != null) {
                        adjacentColor = tiles[r][c - 1].getColor();
                    }
                    do {
                        color = colors[random.nextInt(numColors)];
                    } while (color == adjacentColor);
                } else {
                    do {
                        color = colors[random.nextInt(numColors)];
                    } while (hasMatchesAt(r, c, color));
                }
                tiles[r][c] = new Tile(color);
            }
        }
        checkAndFillEmptyTiles();
    }

    /*Mas tu takych vela opakovanych casti kodu. Taketo algoritmy sa daju refaktorovat podobnym sposobom, ako sme robili
    metodu countAdjacentMines alebo openAdjacentTiles v Minach. Sprav si pomocnu metodu countConsecutiveTiles,
    ktorej zadas riadok a stlpec, kde sa ma zacat a offset zaciatku a konca v riadku/stlpci, ktori sa ma checknut
    Ak sa maju checknut tri tiles v riadku 5, tak offset riadka bude 0 pre koniec aj zaciatok a offset pre stlpec bude pre start -2 a pre end +2.
    Takto vies spravit jednu genericku metodu, ktora prejde vsetko bud len v riadku alebo len v slpci, podla toho ake offsety jej zadas :)
    */
    private boolean hasMatchesAt(int row, int col, TileColor color) {
        // Check horizontal matches
        int numConsecutive = 1;
        for (int c = col - 1; c >= Math.max(0, col - 2) && tiles[row][c].getColor() == color; c--) {
            numConsecutive++;
        }
        for (int c = col + 1; c <= Math.min(numCols - 1, col + 2) && tiles[row][c].getColor() == color; c++) {
            numConsecutive++;
        }
        if (numConsecutive >= 3) {
            return true;
        }

        // Check vertical matches
        numConsecutive = 1;
        for (int r = row - 1; r >= Math.max(0, row - 2) && tiles[r][col].getColor() == color; r--) {
            numConsecutive++;
        }
        for (int r = row + 1; r <= Math.min(numRows - 1, row + 2) && tiles[r][col].getColor() == color; r++) {
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
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (r == 8 && c == 8 || r == 8 && c == 9 || r == 9 && c == 8 || r == 9 && c == 9) {
                    if (tiles[r][c] == null) {
                        // Fill the empty tile with a color that won't trigger a match-3
                        TileColor color;

                        do {
                            color = colors[new Random().nextInt(colors.length)];
                        } while (invalidColors.contains(color));

                        invalidColors.add(color);
                        tiles[r][c] = new Tile(color);
                    }
                }
            }
        }
    }
}

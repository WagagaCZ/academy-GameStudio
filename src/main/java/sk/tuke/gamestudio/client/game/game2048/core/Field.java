package sk.tuke.gamestudio.client.game.game2048.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Field implements Serializable {
    private final Tile[][] tiles;
    private final int rowCount;
    private final int columnCount;
    private GameState state = GameState.PLAYING;
    /**
     * Largest value in array, used for game win logic
     */
    private int currLargest;
    /**
     * Scores is sum of all tiles minus number of moves
     */
    private int score;
    private int numOfMoves;
    private final int WIN_VALUE = 2048;
    private boolean moveHappened = false;
    /**
     * Constructor for Field class
     *
     * @param rowCount    number of rows in field
     * @param columnCount number of columns in field
     */
    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.numOfMoves = 0;
        this.score = 0;
        currLargest = 0;
        tiles = new Tile[rowCount][columnCount];
        generate();
    }

    /**
     * Generates playing field and two random 2 value tiles
     */
    private void generate() {
        for (int r = 0; r < getRowCount(); ++r) {
            for (int c = 0; c < getColumnCount(); ++c) {
                tiles[r][c] = new Tile(r, c, 0);
            }
        }
        // generates random tile
        generateRound();
        generateRound();
    }

    /**
     * Finds all free spaces inside of playing field
     * @return List of coordinates of those free spaces
     */
    private List<Coord> findFreeSpaces() {
        List<Coord> free_spaces = new ArrayList<>();
        for (int r = 0; r < getRowCount(); ++r) {
            for (int c = 0; c < getColumnCount(); ++c) {
                if (tiles[r][c].isEmpty()) {
                    free_spaces.add(new Coord(r, c));
                    continue;
                }
                if (tiles[r][c].getValue() > currLargest)
                    currLargest = tiles[r][c].getValue();
            }
        }
        return free_spaces;
    }

    /**
     * Checks if there is at least one possible move, used for determining game end
     * @return true if there is at least 2 adjacent same value tiles
     */
    private boolean isMovePossible() {
        for (int r = 0; r < getRowCount(); ++r) {
            for (int c = 0; c < getColumnCount(); ++c) {
                int value = tiles[r][c].getValue();
                if ((r + 1 < getRowCount() && value == tiles[r + 1][c].getValue())
                   || (r - 1 >= 0 && value == tiles[r - 1][c].getValue())
                   || (c + 1 < getColumnCount() && value == tiles[r][c + 1].getValue())
                   || (c - 1 >= 0 && value == tiles[r][c - 1].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generate random tile in free space inside field with value of 2
     */
    private void generateRound() {
        List<Coord> freeSpaces = findFreeSpaces();
        int pickCoord = ThreadLocalRandom.current().nextInt(freeSpaces.size());
        int x = freeSpaces.get(pickCoord).getX();
        int y = freeSpaces.get(pickCoord).getY();
        tiles[x][y].setValue(2);
    }

    /**
     * Main game loop method, moves the board and if tiles shifted generates new ones, also checks if game is solved or failed
     * @param direction direction in which the board will shift
     * @return false if game ended, true if not
     */
    public boolean doMove(Direction direction) {
        // TODO this can be done much cheaper with more brain power
        // :D
        int[][] tilesBeforeMove = new int[getRowCount()][getColumnCount()];
        for (int r = 0; r < getRowCount(); ++r) {
            for (int c = 0; c < getColumnCount(); ++c) {
                tilesBeforeMove[r][c] = tiles[r][c].getValue();
            }
        }

        switch (direction) {
            case UP -> calculateMoveUP();
            case DOWN -> calculateMoveDOWN();
            case LEFT -> calculateMoveLEFT();
            case RIGHT -> calculateMoveRIGHT();
        }

        /* da sa to spravit takto:
         - metoda calculateMove(direction) bude v cykle na zaklade smeru vyberat jednotlive riadky/stlpce z hracieho pola ako jednorozmerne polia int[]
         - ak ide o UP/DOWN, tak vytiahne stlpec, ak ide o LEFT/RIGHT, vytiahne riadok
         - pokial ide o backward smer (LEFT/UP), prevrati poradie prvkov v danom poli int[]
         - pre dany riadok vykona shiftTilesRight(int[]), potom mergeTiles(int[]) a znova shirtTilesRight(int[]) - na toto nam stacia dve genericke metody, ktore budu robit vzdy to iste
         - nasledne vezme vysledok a ulozi ho spat do hracieho pola.
         */


        // last compute score before game end
        score = computeScore();

        if (currLargest == WIN_VALUE)
            state = GameState.SOLVED;

        // if something in tiles[][] changed its position we can generate new tiles
        if (moveHappened(tilesBeforeMove, tiles)) {
            numOfMoves++;
            generateRound();
        }

        // the board is full and we cant move in the next move
        if (findFreeSpaces().size() == 0 && !isMovePossible()) {
            state = GameState.FAILED;
            return false;
        }

        return true;
    }
    private int computeScore() {
        int res = 0;
        for ( int i = 0; i < getRowCount(); ++i )
            for ( int j = 0; j < getColumnCount(); ++j )
                res += tiles[i][j].getValue();
        return ( (2 * res) - numOfMoves );
    }
    /**
     * Checks if two arrays are the same in terms of contents
     * @return false if the arrays have the same contents, true if they have at least one different tile value
     */
    private boolean moveHappened(int[][] beforeArr, Tile[][] currArr) {
        for (int r = 0; r < getRowCount(); ++r) {
            for (int c = 0; c < getColumnCount(); ++c) {
                if (beforeArr[r][c] != currArr[r][c].getValue())
                    return true;
            }
        }
        return false;
    }

    /**
     * Shifts and merges the tiles in board in specified direction
     */
    public void calculateMoveUP() {
        for (int column = 0; column < columnCount; ++column) {
            for (int row = 0; row < rowCount; ++row) {
                Tile anchorTile = tiles[row][column];
                // first merge as much a possible, then slide
                if (!anchorTile.isEmpty()) {
                    // anchor is not empty, find first tile to merge with
                    for (int i = row + 1; i < rowCount; ++i) {
                        if (!tiles[i][column].isEmpty()) {
                            anchorTile.mergeWith(tiles[i][column]);
                            break;
                        }
                    }
                }
            }
        }
        for (int column = 0; column < columnCount; ++column) {
            for (int row = 0; row < rowCount; ++row) {
                Tile anchorTile = tiles[row][column];
                // first merge as much a possible, then slide
                if (anchorTile.isEmpty()) {
                    for (int i = row + 1; i < rowCount; ++i) {
                        if (!tiles[i][column].isEmpty()) {
                            Tile.swapValues(anchorTile, tiles[i][column]);
                            break;
                        }
                    }
                }
            }
        }
    }
    /**
     * Shifts and merges the tiles in board in specified direction
     */
    public void calculateMoveDOWN() {
        for (int column = 0; column < columnCount; ++column) {
            for (int row = rowCount - 1; row >= 0; --row) {
                Tile anchorTile = tiles[row][column];
                if (!anchorTile.isEmpty()) {
                    // anchor is not empty, find first tile to merge with
                    for (int i = row - 1; i >= 0; --i) {
                        if (!tiles[i][column].isEmpty()) {
                            anchorTile.mergeWith(tiles[i][column]);
                            break;
                        }
                    }
                }
            }
        }
        for (int column = 0; column < columnCount; ++column) {
            for (int row = rowCount - 1; row >= 0; --row) {
                Tile anchorTile = tiles[row][column];
                if (anchorTile.isEmpty()) {
                    // anchor is empty value find first tile to swap with
                    for (int i = row - 1; i >= 0; --i) {
                        if (!tiles[i][column].isEmpty()) {
                            Tile.swapValues(anchorTile, tiles[i][column]);
                            break;
                        }
                    }
                }
            }
        }
    }
    /**
     * Shifts and merges the tiles in board in specified direction
     */
    public void calculateMoveLEFT() {
        for (int row = 0; row < rowCount; ++row) {
            for (int column = 0; column < columnCount; ++column) {
                Tile anchorTile = tiles[row][column];
                if (!anchorTile.isEmpty()) {
                    // anchor is not empty, find first tile to merge with
                    for (int i = column + 1; i < columnCount; ++i) {
                        if (!tiles[row][i].isEmpty()) {
                            anchorTile.mergeWith(tiles[row][i]);
                            break;
                        }

                    }
                }
            }
        }
        for (int row = 0; row < rowCount; ++row) {
            for (int column = 0; column < columnCount; ++column) {
                Tile anchorTile = tiles[row][column];
                if (anchorTile.isEmpty()) {
                    // anchor is empty value find first tile to swap with
                    for (int i = column + 1; i < columnCount; ++i) {
                        if (!tiles[row][i].isEmpty()) {
                            Tile.swapValues(anchorTile, tiles[row][i]);
                            break;
                        }
                    }
                }
            }
        }
    }
    /**
     * Shifts and merges the tiles in board in specified direction
     */
    public void calculateMoveRIGHT() {
        for (int row = 0; row < rowCount; ++row) {
            for (int column = columnCount - 1; column >= 0; --column) {
                Tile anchorTile = tiles[row][column];
                if (!anchorTile.isEmpty()) {
                    // anchor is not empty, find first tile to merge with
                    for (int i = column - 1; i >= 0; --i) {
                        if (!tiles[row][i].isEmpty()) {
                            anchorTile.mergeWith(tiles[row][i]);
                            break;
                        }
                    }
                }
            }
        }
        for (int row = 0; row < rowCount; ++row) {
            for (int column = columnCount - 1; column >= 0; --column) {
                Tile anchorTile = tiles[row][column];
                if (anchorTile.isEmpty()) {
                    // anchor is empty value find first tile to swap with
                    for (int i = column - 1; i >= 0; --i) {
                        if (!tiles[row][i].isEmpty()) {
                            Tile.swapValues(anchorTile, tiles[row][i]);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if there is a tile with specified value
     */
    public boolean isSolved() {
        return currLargest >= 2048;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    /**
     * @return current game state as GameState enum (PLAYING/FAILED/SOLVED)
     */
    public GameState getState() {
        return state;
    }

    /**
     * @return board as 2d Tile array
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * @return score
     */
    public int getScore() {
        score = computeScore();
        return score;
    }

    /**
     * @return Tile array for printing
     */
    @Override
    public String toString() {
        Formatter f = new Formatter();
        int columnSize = (int) (Math.log10(getColumnCount()) + 1) + 2;
        var format = "%" + columnSize + "s";

        for (int r = 0; r < getRowCount(); ++r) {
            for (int c = 0; c < getColumnCount(); ++c) {
                f.format(format, tiles[r][c].toString());
            }
            f.format("\n");
        }
        return f.toString();
    }
}

package sk.tuke.gamestudio.client.game.minesweeper.core;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Field represents playing field and game logic.
 */
public class Field {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    private final long startTimeInMs;
    private int score;

    private boolean justFinished=false;

    private boolean marking=false;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        if (rowCount * columnCount <= mineCount)
            throw new IllegalArgumentException("Invalid number of mines in the field");

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();

        startTimeInMs = System.currentTimeMillis();
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Clue
                    && ((Clue) tile).getValue() == 0) {
                getOpenAdjacentTiles(row, column);
            }

            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }

            if (isSolved()) {
                state = GameState.SOLVED;
                score = computeScore();
            }
        }
    }

    private void getOpenAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        var t = getTile(row, column);
        if (t.getState() == Tile.State.CLOSED) t.setState(Tile.State.MARKED);
        else if (t.getState() == Tile.State.MARKED) t.setState(Tile.State.CLOSED);
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        var count = 0;
//        Random rand = new Random(); //old way until Java 1.7
        do {
//            int row = rand.nextInt(rowCount); //old way
//            int col = rand.nextInt(columnCount); //old way
            var row = ThreadLocalRandom.current().nextInt(rowCount);
            var col = ThreadLocalRandom.current().nextInt(columnCount);

            if (getTile(row, col) == null) {
                tiles[row][col] = new Mine();
                count++;
            }
        } while (count < mineCount);

        for (var r = 0; r < rowCount; r++) {
            for (var c = 0; c < columnCount; c++) {
                if (tiles[r][c] == null)
                    tiles[r][c] = new Clue(countAdjacentMines(r, c));
            }
        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    private boolean isSolved() {
        return getNumberOf(Tile.State.OPEN) == (rowCount * columnCount) - mineCount;
    }

    private int getNumberOf(Tile.State state) {
        var count = 0;

        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t.getState() == state) {
                    count++;
                }
            }
        }
        return count;
//        alternativne cez stream:
//        return Arrays.stream(tiles)
//                .flatMap(Arrays::stream)
//                .filter(tile -> tile.getState() == state)
//                .toList()
//                .size();
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        var count = 0;
        for (var rowOffset = -1; rowOffset <= 1; rowOffset++) {
            var actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (var columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    var actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    private int computeScore() {
        int score = 0;
        if (state == GameState.SOLVED) {
            score = rowCount * columnCount * 10 -
                    (int) ((System.currentTimeMillis() - startTimeInMs) / 1000);
            if (score < 0) score = 0;
        }
        return score;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getScore() {
        return score;
    }

    public boolean isJustFinished() {
        return justFinished;
    }

    public void setJustFinished(boolean justFinished) {
        this.justFinished = justFinished;
    }

    public boolean isMarking() {
        return marking;
    }

    public void setMarking(boolean marking) {
        this.marking = marking;
    }
}

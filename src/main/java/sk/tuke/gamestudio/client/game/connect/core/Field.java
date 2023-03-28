package sk.tuke.gamestudio.client.game.connect.core;

import sk.tuke.gamestudio.client.game.connect.core.GameState;

public class Field {
    private int rows;
    private int cols;
    private int score=0;
    private Tile[][] tiles;
    private GameState state = GameState.PLAYING;

    private long startTimeInMs;

    private int currentPlayer;

    public Field(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        tiles = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = new Tile();
            }
        }
        currentPlayer = 1;
        startTimeInMs = System.currentTimeMillis();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public boolean isFull() {
        for (int i = 0; i < cols; i++) {
            if (tiles[0][i].getPlayer() == 0) {
                return false;
            }
        }
        state = GameState.FULL;
        return true;
    }

    public boolean isValidMove(int col) {
        if (col < 0 || col >= cols) {
            return false;
        }
        return tiles[0][col].getPlayer() == 0;
    }

    public boolean makeMove( int col) {
        if (!isValidMove(col)) {
            return false;
        }
        for (int i = rows - 1; i >= 0; i--) {
            if (tiles[i][col].getPlayer() == 0) {
                tiles[i][col].setPlayer(currentPlayer);
                if(checkForWin(currentPlayer)){
                    score = computeScore();
                    if (currentPlayer == 1)
                        state = GameState.P1WIN;
                    else
                        state = GameState.P2WIN;
                };
                switchPlayer();
                return true;
            }
        }
        return false;
    }

    public boolean checkForWin(int player) {
        // Check for horizontal win
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j <= this.cols - 4; j++) {
                if (this.tiles[i][j].getPlayer() == player &&
                        this.tiles[i][j + 1].getPlayer() == player &&
                        this.tiles[i][j + 2].getPlayer() == player &&
                        this.tiles[i][j + 3].getPlayer() == player) {
                    return true;
                }
            }
        }

        // Check for vertical win
        for (int i = 0; i <= this.rows - 4; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.tiles[i][j].getPlayer() == player &&
                        this.tiles[i + 1][j].getPlayer() == player &&
                        this.tiles[i + 2][j].getPlayer() == player &&
                        this.tiles[i + 3][j].getPlayer() == player) {
                    return true;
                }
            }
        }

        // Check for diagonal win (top-left to bottom-right)
        for (int i = 0; i <= this.rows - 4; i++) {
            for (int j = 0; j <= this.cols - 4; j++) {
                if (this.tiles[i][j].getPlayer() == player &&
                        this.tiles[i + 1][j + 1].getPlayer() == player &&
                        this.tiles[i + 2][j + 2].getPlayer() == player &&
                        this.tiles[i + 3][j + 3].getPlayer() == player) {
                    return true;
                }
            }
        }

        // check diagonal (bottom left to top right)
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (tiles[row][col].getPlayer() == player
                        && tiles[row - 1][col + 1].getPlayer() == player
                        && tiles[row - 2][col + 2].getPlayer() == player
                        && tiles[row - 3][col + 3].getPlayer() == player) {
                    return true;
                }
            }
        }

        // no win found
        return false;
    }

    private int computeScore() {
        int score = 0;
            score = rows * cols * 10 -
                    (int) ((System.currentTimeMillis() - startTimeInMs) / 1000);
            if (score < 0) score = 0;
        return score;
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    public int getScore() {
        return score;
    }

    public GameState getState() {
        return state;
    }
}

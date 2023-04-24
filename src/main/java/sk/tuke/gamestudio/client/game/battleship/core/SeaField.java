package sk.tuke.gamestudio.client.game.battleship.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SeaField {
    private final Tile[][] playerTiles;
    private final Tile[][] enemyTiles;
    private GameState state = GameState.PLAYING;
    private final int rowCount = 10;
    private final int columnCount = 10;
    private int numberOfTries;
    private int numberOfPlayerShip = 25;
    private int numberOfEnemyShip = 25;

    public SeaField() {
        playerTiles = new Tile[rowCount][columnCount];
        enemyTiles = new Tile[rowCount][columnCount];
        numberOfTries = 0;
        generate();
    }

    public GameState getState() {
        return state;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getNumberOfTries() {
        return numberOfTries;
    }

    public Tile getTile(Tile[][]tiles,int row, int column) {
        return tiles[row][column];
    }

    public Tile[][] getPlayerTiles() {
        return playerTiles;
    }

    public Tile[][] getEnemyTiles() {
        return enemyTiles;
    }

    public int getNumberOfPlayerShip() {
        return numberOfPlayerShip;
    }

    public int getNumberOfEnemyShip() {
        return numberOfEnemyShip;
    }


    public void generate() {
        createPlayingField(this.playerTiles);
        createPlayingField(this.enemyTiles);
    }

    public void createPlayingField(Tile[][] tiles) {
        Random random = new Random();
        List<Integer> shipSizes = Arrays.asList(5, 4, 4, 3, 3, 2, 2, 2);
        Collections.shuffle(shipSizes);

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(rowCount);
                int col = random.nextInt(columnCount);
                boolean horizontal = random.nextBoolean();
                boolean validPlacement = canPlaceShip(tiles, row, col, size, horizontal);

                if (validPlacement) {
                    for (int i = row - 1; i <= row + 1; i++) {
                        for (int j = col - 1; j <= col + 1; j++) {
                            if (i >= 0 && i < rowCount && j >= 0 && j < columnCount) {
                                if (tiles[i][j] instanceof Ship) {
                                    validPlacement = false;
                                    break;
                                }
                            }
                        }
                        if (!validPlacement) {
                            break;
                        }
                    }
                }

                if (validPlacement) {
                    placeShip(tiles, row, col, size, horizontal);
                    placed = true;
                }
            }
        }

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j] == null) {
                    tiles[i][j] = new Tile();
                }
            }
        }
    }

    private boolean canPlaceShip(Tile[][] tiles, int row, int col, int size, boolean horizontal) {
        //toto sa da cele zrefaktorovat podobne ako placeShip :)
        if (horizontal) {
            if (col + size > columnCount) {
                return false;
            }
            for (int c = col; c < col + size; c++) {
                if (tiles[row][c] != null
                        || row > 0 && tiles[row - 1][c] != null
                        || row < rowCount - 1 && tiles[row + 1][c] != null
                        || c > 0 && tiles[row][c - 1] != null
                        || c < columnCount - 1 && tiles[row][c + 1] != null) {
                    return false;
                }
            }
        } else {
            if (row + size > rowCount) {
                return false;
            }
            for (int r = row; r < row + size; r++) {
                if (tiles[r][col] != null
                        || col > 0 && tiles[r][col - 1] != null
                        || col < columnCount - 1 && tiles[r][col + 1] != null
                        || r > 0 && tiles[r - 1][col] != null
                        || r < rowCount - 1 && tiles[r + 1][col] != null) {
                    return false;
                }
            }
        }
        return true;
    }


    private void placeShip(Tile[][]tiles, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            for (int c = col; c < col + size; c++) {
                tiles[row][c] = new Ship();
            }
        } else {
            for (int r = row; r < row + size; r++) {
                tiles[r][col] = new Ship();
            }
        }

//        for (int i = 0; i < size; i++) {
//            int r = row + (horizontal ? 0 : i);
//            int c = col + (horizontal ? i : 0);
//            tiles[r][c] = new Ship();
//        }
    }

    public void openTile(int row, int column) {
        System.out.println(">>>>" + row + " " + column);
        System.out.println(enemyTiles[row][column]);
        Tile tile = enemyTiles[row][column];

        if (tile.getState() == Tile.State.WATER) {
            if(tile instanceof Ship) {
                tile.setState(Tile.State.HIT);
                --numberOfEnemyShip;
            } else {
                tile.setState(Tile.State.MISS);
            }

            numberOfTries++;

            if (isSolved()) {
                state = GameState.WON;
            }
        }
    }

    public void opponentOpenTile() {
        Random random = new Random();
        int row, col;
        Tile tile;

        do {
            row = random.nextInt(rowCount);
            col = random.nextInt(columnCount);
            tile = playerTiles[row][col];
        } while (tile == null || tile.getState() != Tile.State.WATER);

        if(tile instanceof Ship){
            tile.setState(Tile.State.HIT);
            numberOfPlayerShip--;
        } else {
            tile.setState(Tile.State.MISS);
        }

        if (isSolved()) {
            state = GameState.FAILED;
        }
    }


    public boolean checkColumnRowInRange(int row, int column) {
        return (row <= rowCount && column + 1 <= columnCount);
    }

    public boolean isSolved() {
        return areAllShipsDestroyed(playerTiles) || areAllShipsDestroyed(enemyTiles);
    }

    public boolean areAllShipsDestroyed(Tile[][]tiles) {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Tile tile = tiles[row][col];
                if (tile instanceof Ship && tile.getState() != Tile.State.HIT) {
                    return false;
                }
            }
        }
        return true;
    }

}

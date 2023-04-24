package sk.tuke.gamestudio.client.game.game2048.core;

import java.io.Serializable;

public class Tile implements Serializable {
    Coord coord;
    // value of 0 is blank tile
    int value;

    /**
     * Constructor
     *
     * @param x     x coordinate represents row number (from zero)
     * @param y     y coordinate represents column number (from zero)
     * @param value value of this tile
     */
    public Tile(int x, int y, int value) {
        this.value = value;
        this.coord = new Coord(x, y);
    }

    /**
     * Increments this object value times two and zeros the param value,
     * if this and param values are the same
     *
     * @param tile tile to be zeroed
     * @return true if merge was successful, false if the two values are not the same
     */
    public boolean mergeWith(Tile tile) {
        if (this.getValue() != tile.getValue())
            return false;
        // this + tile values
        this.value *= 2;
        // tile.value is now nothing
        tile.value = 0;
        return true;
    }

    /**
     * Swaps the two parameter tiles values
     */
    public static void swapValues(Tile tile1, Tile tile2) {
        int tmp = tile1.getValue();
        tile1.setValue(tile2.getValue());
        tile2.setValue(tmp);
    }

    /**
     * Checks if this tiles value is zero
     */
    public boolean isEmpty() {
        return value == 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return Coord object representing position in board
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     * @return value or - if the value is zero
     */
    @Override
    public String toString() {
        return value != 0 ? String.valueOf(value) : "-";
    }
}

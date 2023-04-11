package sk.tuke.gamestudio.client.game.game2048.core;

import java.io.Serializable;

/**
 * Coordinates class
 */
public class Coord implements Serializable {
    private int x;
    private int y;
    public Coord( int x, int y ) {
        this.x = x;
        this.y = y;
    }
    /**
     * Check if coordinate is inside boundaries
     * @param minX minimum value of X coordinate ( inclusive )
     * @param maxX maximum value of X coordinate ( exclusive )
     * @param minY minimum value of Y coordinate ( inclusive )
     * @param maxY maximum value of Y coordinate ( exclusive )
     * @return true if coordinate is inside boundaries
     */
    public boolean checkBoundaries( int minX, int maxX, int minY, int maxY ) {
        return getX() >= minX && getX() < maxX && getY() >= minY && getY() < maxY;
    }
    /**
     * Getter for x coordinate
     * @return
     */
    public int getX() {
        return x;
    }
    /**
     * Getter for y coordinate
     * @return
     */
    public int getY() {
        return y;
    }
}

package sk.tuke.gamestudio.client.game.minesweeper.core;

/**
 * Mine tile.
 */
public class Mine extends Tile {
    @Override
    public String toString() {
        if(getState() != State.OPEN) return super.toString();
        return "*";
    }
}

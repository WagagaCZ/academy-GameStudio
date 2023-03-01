package sk.tuke.gamestudio.game.minesweeper.core;

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

package sk.tuke.gamestudio.client.game.minesweeper.core;

/**
 * Tile of a field.
 */
public abstract class Tile {
    
    /** Tile states. //Tile.State */
    public enum State {
        /** Open tile. */
        OPEN, 
        /** Closed tile. */
        CLOSED,
        /** Marked tile. */
        MARKED
    }
    
    /** Tile state. */
    private State state = State.CLOSED;
        
    /**
     * Returns current state of this tile.
     * @return current state of this tile
     */
    public State getState() {
        return state;
    }

    /**
     * Sets current current state of this tile.
     * @param state current state of this tile
     */
    void setState(State state) {
        this.state = state;
    }

    public boolean isNotOpen(){
        return this.state != State.OPEN;
    }

    @Override
    public String toString() {
        if(this.state == Tile.State.CLOSED) return "-";
        if(this.state == Tile.State.MARKED) return "M";
        return " ";
    }
}

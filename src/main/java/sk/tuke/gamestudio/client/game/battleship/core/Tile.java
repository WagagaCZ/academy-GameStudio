package sk.tuke.gamestudio.client.game.battleship.core;

public class Tile {
    public enum State {
        WATER,
        MISS,
        HIT
    }

    private State state = State.WATER;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isNotOpen(){
        return this.state == State.WATER;
    }


    public String toString() {
        return switch (state) {
            case WATER -> "~";
            case HIT -> "X";
            case MISS -> "O";
        };
    }
}


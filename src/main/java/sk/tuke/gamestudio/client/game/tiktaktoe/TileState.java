package sk.tuke.gamestudio.client.game.tiktaktoe;

public enum TileState {
    EMPTY(" "), //parametricky enum, pekne :)
    CROSS("X"),
    ZERO("0");

    private final String stateTile;
    TileState(String stateTile) {
        this.stateTile = stateTile;
    }
    public String getStateTile() {
        return stateTile;
    }
}

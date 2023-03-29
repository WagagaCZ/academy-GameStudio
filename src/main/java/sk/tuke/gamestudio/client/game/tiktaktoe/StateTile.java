package sk.tuke.gamestudio.client.game.tiktaktoe;

public enum StateTile {
    EMPTY(" "),
    CROSS("X"),
    ZERO("0");

    private final String stateTile;
    StateTile(String stateTile) {
        this.stateTile=stateTile;
    }
    public String getStateTile() {
        return stateTile;
    }
}

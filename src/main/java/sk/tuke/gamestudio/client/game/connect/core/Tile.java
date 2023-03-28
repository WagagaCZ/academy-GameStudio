package sk.tuke.gamestudio.client.game.connect.core;

public class Tile {
    private int player; // 0: empty, 1: player 1, 2: player 2

    public Tile() {
        player = 0; // empty by default
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
package sk.tuke.gamestudio.client.game.candycrush.core;


public class Tile {
    private TileColor color;

    public Tile() {
        this.color = TileColor.EMPTY;
    }

    public Tile(TileColor color) {
        this.color = color;
    }

    public TileColor getColor() {
        return color;
    }

    public void setColor(TileColor color) {
        this.color = color;
    }
}

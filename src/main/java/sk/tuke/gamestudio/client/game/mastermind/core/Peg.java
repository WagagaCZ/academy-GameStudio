package sk.tuke.gamestudio.client.game.mastermind.core;

public class Peg {
    private PegColor color;
    private boolean hidden;

    public Peg(PegColor color, boolean hidden) {
        this.color = color;
        this.hidden = hidden;
    }

    public Peg(PegColor color) {
        this.color = color;
        this.hidden = false;
    }

    @Override
    public String toString() {
        if (hidden) {
            return "( ? )";
        } else {
            return "( " + color.ordinal() + " )";
        }
    }

    public PegColor getColor() {
        return color;
    }

    public void setColor(PegColor color) {
        this.color = color;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

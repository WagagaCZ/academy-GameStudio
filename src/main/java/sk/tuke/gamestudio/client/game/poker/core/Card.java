package sk.tuke.gamestudio.client.game.poker.core;

public class Card {
    public enum Type {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Color {
        RED, BLACK
    }

    //Value of a card - from 2 up to 14 (ace)
    private int value;
    private Type type;
    private Color color;
    private String path;
    private boolean selected;

    public Card(int value, Type type, Color color, String path) {
        this.value = value;
        this.type = type;
        this.color = color;
        this.path = path;
        this.selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getValueString() {
        return switch(value) {
            case 1 -> "A";
            case 13 -> "K";
            case 12 -> "Q";
            case 11 -> "J";
            default -> String.valueOf(value);
        };
    }

    @Override
    public String toString() {
        return "value=" + getValueString() +
                ", type=" + type +
                ", color=" + color +
                ';';
    }
}

package sk.tuke.gamestudio.client.game.pexeso.core;

public class Card {
    private int value;
    private boolean isFlipped = false;

    public Card() {}

    public Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isFlipped() {
        return this.isFlipped;
    }
    public void flip(){
        isFlipped = !isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Card)) {
            return false;
        }

        Card other = (Card) obj;

        return this.value == other.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

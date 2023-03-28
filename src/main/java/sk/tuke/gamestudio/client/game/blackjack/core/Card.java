package sk.tuke.gamestudio.client.game.blackjack.core;

public class Card {
    private int id;
    private int category;

    public Card(int id, int category) {
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }
    //TODO: toString se správným výpisem znaků

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", category=" + category +
                '}';
    }
}

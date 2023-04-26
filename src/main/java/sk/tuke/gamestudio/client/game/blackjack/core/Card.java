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

    @Override
    public String toString() {
        if(category == -1) return "?";

        StringBuilder sb = new StringBuilder();

        sb.append((char) switch(category) {
            case 0 -> 9824;
            case 1 -> 9829;
            case 2 -> 9827;
            case 3 -> 9830;
            default -> 0;
        });

        sb.append((char) switch(id) {
            case 1 -> 65;
            case 11 -> 74;
            case 12 -> 81;
            case 13 -> 75;
            default -> (48 + id);
        });

        return sb.toString();
        /* neviem ci tu este nebude niekde nejaky bug,
           lebo v jednom pripade mi to vypisuje dvojbodku,
           ale takto nejako by som to refaktorovala */
    }
}

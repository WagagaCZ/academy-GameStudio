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
        char cid = '0';
        char iid = '0';
        StringBuilder finale = new StringBuilder();
        switch (category){
            case -1: {
                return "?";
            }
            case 0: {
                cid = 9824;
                break;
            }
            case 1: {
                cid = 9829;
                break;
            }
            case 2: {
                cid = 9827;
                break;
            }
            case 3: {
                cid = 9830;
                break;
            }
        }
        finale.append(cid);
        switch (id){
            case 1: {
                iid = 65;
                break;
            }
            case 11: {
                iid = 74;
                break;
            }
            case 12: {
                iid = 81;
                break;
            }
            case 13: {
                iid = 75;
                break;
            }
            default: {
                iid = (char) (48+id);
                break;
            }
        }
        finale.append(iid);
        return finale.toString();
    }
}

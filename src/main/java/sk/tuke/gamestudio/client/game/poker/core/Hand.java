package sk.tuke.gamestudio.client.game.poker.core;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    private List<Card> hand = new ArrayList<>();
    public Hand(){

    }

    public void draw(Deck deck, int num){
        for (int i = 0; i < num; i++) {
            Card card = deck.deck.remove(i);
            hand.add(card);
        }
    }

    @Override
    public String toString() {
        return "Hand{" +
                "hand=" + hand +
                '}';
    }
}

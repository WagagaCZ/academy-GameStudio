package sk.tuke.gamestudio.client.game.blackjack.core;


import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Deck {
    private final Card[] deck;
    private int leftInDeck = 52;

    public Deck() {
        deck = new Card[52];
        
        for (int shape = 0; shape < 4; shape++) { //Trvalo mi dobru minutu kym som pochopila, co tu vlastne robis. Spravne nazvy lepsie komunikuju vyznam kodu.
            for (int card = 1; card <= 13; card++) {
                this.deck[(card - 1) + (shape * 13)] = new Card(card, shape);
            }
        }
    }

    public Card drawCard() {
        Card drawn;
        int pick = ThreadLocalRandom.current().nextInt(51);

        if (deck[pick] != null) {
            drawn = deck[pick];
            deck[pick] = null;
            leftInDeck--;
        } else {
            drawn = drawCard();
        }

        return drawn;
    }

    public Card[] getDeck() {
        return deck;
    }

    public int getLeftInDeck() {
        return leftInDeck;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + Arrays.toString(deck) +
                ", leftInDeck=" + leftInDeck +
                '}';
    }
}

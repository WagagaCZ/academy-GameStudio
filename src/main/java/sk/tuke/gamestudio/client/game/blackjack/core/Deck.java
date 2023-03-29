package sk.tuke.gamestudio.client.game.blackjack.core;


import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Deck {
    private final Card[] deck;
    private int leftInDeck = 52;

    public Deck() {
        deck = new Card[52];
        for(int i = 0; i<4;i++){
            for(int z = 1; z<=13; z++){
                deck[(z-1) + (i*13)] = new Card(z,i);
            }
        }
    }
    public Card drawCard(){
        Card drawn;
        int pick = ThreadLocalRandom.current().nextInt(51);
        if(deck[pick] != null){
            drawn = deck[pick];
            deck[pick] = null;
            leftInDeck--;
        }
        else {
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

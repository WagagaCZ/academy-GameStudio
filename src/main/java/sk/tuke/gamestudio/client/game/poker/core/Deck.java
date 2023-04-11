package sk.tuke.gamestudio.client.game.poker.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    public List<Card> deck = new ArrayList<>();
    String path = "images/cards/";
    public Deck(){
        for (int i = 1; i < 14; i++) {

            deck.add(new Card(i, Card.Type.DIAMONDS, Card.Color.RED, path+"red_"+"diamond_"+i+".png"));
            deck.add(new Card(i, Card.Type.HEARTS, Card.Color.RED, path+"red_"+"heart_"+i+".png"));
            deck.add(new Card(i, Card.Type.SPADES, Card.Color.BLACK, path+"black_"+"spade_"+i+".png"));
            deck.add(new Card(i, Card.Type.CLUBS, Card.Color.BLACK, path+"black_"+"club_"+i+".png"));
        }
        Collections.shuffle(deck);

    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }
}

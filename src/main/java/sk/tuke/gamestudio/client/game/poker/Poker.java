package sk.tuke.gamestudio.client.game.poker;

import sk.tuke.gamestudio.client.game.poker.consoleui.ConsoleUI;
import sk.tuke.gamestudio.client.game.poker.consoleui.UserInterface;
import sk.tuke.gamestudio.client.game.poker.core.Deck;
import sk.tuke.gamestudio.client.game.poker.core.Hand;

public class Poker {
    public static void main(String[] args) {
        Deck deck = new Deck();
        Hand hand = new Hand();

        System.out.println(deck.deck.size());
        hand.draw(deck, 5);
        System.out.println(deck.deck.size());
        System.out.println(hand);
        UserInterface userInterface = new ConsoleUI();
        userInterface.newGameStarted(deck, hand);
    }
}

package sk.tuke.gamestudio.client.game.poker.consoleui;

import sk.tuke.gamestudio.client.game.poker.core.Deck;
import sk.tuke.gamestudio.client.game.poker.core.Hand;

public interface UserInterface {
    void newGameStarted(Deck deck, Hand hand);

    void update();
}

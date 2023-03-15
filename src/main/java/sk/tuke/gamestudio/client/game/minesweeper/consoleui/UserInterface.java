package sk.tuke.gamestudio.client.game.minesweeper.consoleui;

import sk.tuke.gamestudio.client.game.minesweeper.core.Field;

public interface UserInterface {
    void newGameStarted(Field field);

    void update();
}

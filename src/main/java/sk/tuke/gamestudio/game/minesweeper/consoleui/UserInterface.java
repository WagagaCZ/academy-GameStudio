package sk.tuke.gamestudio.game.minesweeper.consoleui;

import sk.tuke.gamestudio.game.minesweeper.core.Field;

public interface UserInterface {
    void newGameStarted(Field field);

    void update();
}

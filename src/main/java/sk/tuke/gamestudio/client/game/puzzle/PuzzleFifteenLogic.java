package sk.tuke.gamestudio.client.game.puzzle;

import sk.tuke.gamestudio.server.controller.PuzzleController;

public class PuzzleFifteenLogic {
    private static final int STEP = 4;
    public PuzzleFifteenLogic() {
    }

    public boolean isCanMoveLeft(int startPositionForMove) {
        return startPositionForMove != 0 && startPositionForMove != (startPositionForMove + STEP)
                && startPositionForMove != (startPositionForMove + STEP * 2)
                && startPositionForMove != (startPositionForMove + STEP * 3);

    }

    public boolean isCanMoveRight(int startPositionForMove) {
        return startPositionForMove != 3 && startPositionForMove != (startPositionForMove +STEP)
                && startPositionForMove != (startPositionForMove + STEP * 2)
                && startPositionForMove != (startPositionForMove + STEP * 3);
    }

    public boolean isCanMoveDown(int startPositionForMove) {
        return startPositionForMove != 12 && startPositionForMove != 13
                && startPositionForMove != 14
                && startPositionForMove != 15;
    }

    public boolean isCanMoveUp(int startPositionForMove) {
        return startPositionForMove != 0 && startPositionForMove != 1
                && startPositionForMove != 2
                && startPositionForMove != 3;

    }
}
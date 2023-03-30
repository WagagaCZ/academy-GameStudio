package sk.tuke.gamestudio.client.game.mastermind.core;

import java.util.Random;

public enum PegColor {
    BLACK,
    GRAY,
    WHITE,
    RED,
    GREEN,
    BLUE,
    YELLOW;

    private final Random random = new Random();

    public PegColor getRandomColor() {
        PegColor[] colors = values();
        return colors[random.nextInt(colors.length)];
    }
}

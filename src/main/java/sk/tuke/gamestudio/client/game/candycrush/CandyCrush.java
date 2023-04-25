package sk.tuke.gamestudio.client.game.candycrush;


import sk.tuke.gamestudio.client.game.candycrush.core.Field;

import java.util.Scanner;

public class CandyCrush {
    private final Field field;

    public CandyCrush(Field field) {
        this.field = field;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            field.display();

            System.out.println("Enter row and column of tile to swap (e.g. 1 2):");
            int row1 = scanner.nextInt();
            int col1 = scanner.nextInt();

            System.out.println("Enter row and column of tile to swap with (e.g. 1 3):");
            int row2 = scanner.nextInt();
            int col2 = scanner.nextInt();

            doSwap(row1, col1, row2, col2);
        }
    }

    private void doSwap(int row1, int col1, int row2, int col2) {
        //toto je cele v podstate core logika hry, mal by to riesit field :)
        if (field.isValidSwap(row1, col1, row2, col2)) {
            field.swap(row1, col1, row2, col2);
            if (field.hasThreeInARow()) {
                field.removeMatches();
            }
        } else {
            System.out.println("Invalid swap!");
        }
    }
}

package sk.tuke.gamestudio.client.game.blackjack;

import sk.tuke.gamestudio.client.game.blackjack.core.Table;

import java.util.Arrays;

public class Blackjack {
    private Blackjack() {
        Table table = new Table();
        table.setup();
        System.out.println("Player:");
        System.out.println(Arrays.toString(table.getPlayerHand()));
        System.out.println("Dealer:");
        System.out.println(Arrays.toString(table.getDealerHand()));
        table.drawNewCard();
        if (table.getTurn() != Table.Turn.END) {
            table.switchTurns();
        }
        table.drawNewCard();
        table.drawNewCard();
        table.drawNewCard();
        System.out.println("Player:");
        System.out.println(Arrays.toString(table.getPlayerHand()));
        System.out.println("Dealer:");
        System.out.println(Arrays.toString(table.getDealerHand()));
    }

    public static void main(String[] args) {
        new Blackjack();
    }
}
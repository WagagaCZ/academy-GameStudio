package sk.tuke.gamestudio.client.game.blackjack;

import sk.tuke.gamestudio.client.game.blackjack.core.Table;
import sk.tuke.gamestudio.client.game.blackjack.core.Turn;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Blackjack {
    private Blackjack() {
        Table table = new Table();
        table.setup();
        
        printCards(table);
        drawCards(table);
        printCards(table);
    }

    private void drawCards(Table table) {
        IntStream.range(0, 4).forEach(i -> table.drawNewCard());

//        table.drawNewCard();
////        if (table.getTurn() != Turn.END) {
////            table.switchTurns();
////        }
//        table.drawNewCard();
//        table.drawNewCard();
//        table.drawNewCard();
    }

    private void printCards(Table table) {
        System.out.println("Player:");
        System.out.println(Arrays.toString(table.getPlayerHand()));
        System.out.println("Dealer:");
        System.out.println(Arrays.toString(table.getDealerHand()));
    }

    public static void main(String[] args) {
        new Blackjack();
    }
}
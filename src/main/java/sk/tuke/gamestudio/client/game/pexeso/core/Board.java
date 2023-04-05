package sk.tuke.gamestudio.client.game.pexeso.core;

import java.util.Random;

public class Board {
    private int column;
    private int row;
    private Card[][] cards;


    public Board(int row, int column) {
        this.row = row;
        this.column = column;
        // Initialize the cards and flipped arrays
        this.cards = new Card[row][column];
        int numPairs = (row * column)/2;

        // Generate a new board with randomized cards
        generateBoard(numPairs);
    }

    public void setCard(int x, int y, Card card){
        if (x >= 0 && x < row && y >= 0 && y < column) {
            cards[x][y] = card;
        } else {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
        }
    }
    public Card getCard(int x, int y) {
        if (x < 0 || x >= row || y < 0 || y >= column) {
            return null;
        }
        return cards[x][y];
    }
    public boolean isFlipped(int x, int y) {
        return x >= 0 && x < row && y >= 0 && y < column && cards[x][y].isFlipped();

    }

    public Card[][] getCards() {
        return cards;
    }

    public void generateBoard(int numPairs) {
        // Create an array of Card objects containing each value twice (to create pairs)
        Card[] cards = new Card[numPairs * 2];
        for (int i = 0; i < numPairs; i++) {
            cards[i] = new Card(i);
            cards[i + numPairs] = new Card(i);
        }

        // Randomize the order of the cards
        Random rand = new Random();
        for (int i = cards.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Card temp = cards[index];
            cards[index] = cards[i];
            cards[i] = temp;
        }


        // Fill the board with the randomized cards
        int index = 0;
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < column; y++) {
                setCard(x, y, cards[index++]);
            }
        }


    }

    public void flipCard(int x, int y) {
        if (x < 0 || x >= row || y < 0 || y >= column) {
            // Coordinates are out of bounds, do something (throw an exception, return, etc.)
            throw new ArrayIndexOutOfBoundsException();
        }

        cards[x][y].flip(); // Flip the card (or unflip it if it's already flipped)

    }
    public boolean isSolved() {
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < column; y++) {
                // If any card is not flipped, the game is not solved yet
                if (!cards[x][y].isFlipped()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}

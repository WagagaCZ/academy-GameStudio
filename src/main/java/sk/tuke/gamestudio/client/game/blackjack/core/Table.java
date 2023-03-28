package sk.tuke.gamestudio.client.game.blackjack.core;

public class Table {
    private final Card[] playerHand;
    private final Card[] dealerHand;
    private int numberOfPlayerCards;
    private int numberOfDealerCards;
    private Deck deck;

    public enum Turn {
        PLAYER,
        DEALER,
        END
    }

    private Turn turn;

    public Table() {
        playerHand = new Card[5];
        dealerHand = new Card[5];
        deck = new Deck();
    }

    public void setup() {
        turn = Turn.PLAYER;
        for (int i = 0; i < 2; i++) {
            playerHand[i] = deck.drawCard();
            dealerHand[i] = deck.drawCard();
        }
        numberOfDealerCards = 2;
        numberOfPlayerCards = 2;
    }

    public void drawNewCard() {
        if (turn == Turn.PLAYER) {
            playerHand[numberOfPlayerCards] = deck.drawCard();
            numberOfPlayerCards++;
            if(checkIfOut(playerHand)){
                System.out.println("Player busted!");
            }
        } else if (turn == Turn.DEALER) {
            dealerHand[numberOfDealerCards] = deck.drawCard();
            numberOfDealerCards++;
            if(checkIfOut(dealerHand)){
                System.out.println("Dealer busted!");
            }
        }
    }

    public void shuffle() {
        deck = new Deck();
    }
    public void switchTurns(){
        turn = Turn.DEALER;
    }
    private boolean checkIfOut(Card[] hand) {
        int sum = 0;
        int numOfAces = 0;
        boolean aceOver = false;
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null) {
                break;
            }
            Card card = hand[i];
            if (card.getId() > 1 && card.getId() < 11) {
                sum += card.getId();
            } else if (card.getId() > 10) {
                sum += 10;
            } else if (card.getId() == 1) {
                numOfAces++;
                if (aceOver) {
                    sum += 1;
                } else {
                    sum += 11;
                }
            }
            if (sum > 21) {
                if (!aceOver && numOfAces > 0) {
                    aceOver = true;
                    sum = sum - (numOfAces * 10);
                } else {
                    turn = Turn.END;
                    return true;
                }
            }
            if (numberOfPlayerCards == 5 && turn == Turn.PLAYER) {
                turn = Turn.DEALER;
            }
            if (numberOfDealerCards == 5 || (turn == Turn.DEALER && sum >= 17)) {
                turn = Turn.END;
            }
        }
        return false;
    }

    public Card[] getPlayerHand() {
        return playerHand;
    }

    public Card[] getDealerHand() {
        return dealerHand;
    }

    public Deck getDeck() {
        return deck;
    }

    public Turn getTurn() {
        return turn;
    }
}

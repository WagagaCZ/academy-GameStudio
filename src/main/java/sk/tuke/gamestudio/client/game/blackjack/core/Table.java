package sk.tuke.gamestudio.client.game.blackjack.core;

public class Table {
    private Card[] playerHand;
    private Card[] dealerHand;
    private int numberOfPlayerCards;
    private int numberOfDealerCards;
    private Deck deck;
    private Turn turn;
    private Card dealerCard;

    public Table() {
        deck = new Deck();
        setup();
    }

    public void setup() {
        if(turn == Turn.END) {
            System.out.println("Cards in deck: " + deck.getLeftInDeck());
            if (deck.getLeftInDeck() < 10) {
                shuffle();
            }
            playerHand = new Card[5];
            dealerHand = new Card[5];
            turn = Turn.PLAYER;
            for (int i = 0; i < 2; i++) {
                playerHand[i] = deck.drawCard();
                dealerHand[i] = deck.drawCard();
            }
            dealerCard = dealerHand[1];
            dealerHand[1] = new Card(-1, -1);
            numberOfDealerCards = 2;
            numberOfPlayerCards = 2;
        }
    }

    public void drawNewCard() {
        if (turn == Turn.PLAYER) {
            playerHand[numberOfPlayerCards] = deck.drawCard();
            numberOfPlayerCards++;
            if (checkIfOut(playerHand)) {
                System.out.println("Player busted!");
            }
        } else if (turn == Turn.DEALER) {
            dealerHand[numberOfDealerCards] = deck.drawCard();
            numberOfDealerCards++;
            if (checkIfOut(dealerHand)) {
                System.out.println("Dealer busted!");
            }
        }
    }

    public void shuffle() {
        if (turn == Turn.END) {
            deck = new Deck();
        } else {
            System.out.println("Can't shuffle, not the end of game.");
        }
    }

    public void switchTurns() {
        if (turn == Turn.PLAYER) {
            dealerHand[1] = dealerCard;
            turn = Turn.DEALER;
            checkIfOut(dealerHand);
        } else {
            System.out.println("Can't switch, not players turn.");
        }
    }

    private boolean checkIfOut(Card[] hand) {
        int sum = checkSum(hand);
        if (sum > 21) {
            turn = Turn.END;
            System.out.println(sum);
            return true;
        }
        if (numberOfPlayerCards == 5 && turn == Turn.PLAYER) {
            System.out.println("Player over hand limit");
            switchTurns();
        }
        if (numberOfDealerCards == 5 || (turn == Turn.DEALER && sum >= 17)) {
            turn = Turn.END;
        }
        System.out.println(sum);
        return false;
    }

    private int checkSum(Card[] hand) {
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
            if (sum > 21 && !aceOver && numOfAces > 0) {
                aceOver = true;
                sum = sum - (numOfAces * 10);
            }
        }
        return sum;
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

    public boolean isPlayerWinner() {
        return (!checkIfOut(playerHand) && (checkSum(playerHand) > checkSum(dealerHand) || checkIfOut(dealerHand)));
    }
}

package sk.tuke.gamestudio.client.game.poker.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.client.game.poker.core.Deck;
import sk.tuke.gamestudio.client.game.poker.core.Hand;
import sk.tuke.gamestudio.client.game.poker.core.Logic;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreException;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Console user interface.
 */
@Component
public class ConsoleUI implements UserInterface {
    /**
     * Playing field.
     */
    private Deck deck;
    private Hand hand;
    private Logic logic;
    private int score;
    private List<Integer> selected = new ArrayList<>();

    /**
     * Regular expression pattern for the user input
     */
    Pattern OPEN_MARK_PATTERN = Pattern.compile("([OM]{1})([A-Z]{1})([0-9]{1,})");

    /**
     * Input reader.
     */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    /**
     * Reads line of text from the reader.
     *
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     */
    @Override
    public void newGameStarted(Deck deck, Hand hand) {
        this.deck = deck;
        this.hand = hand;
        logic = new Logic();

        for (int i = 0; i < 3; i++) {


            update();
            processInput();}


        System.out.println("FINAL SCORE IS: " + score);
        System.exit(0);
    }


    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        System.out.println("Cards selected to change: " + (selected));
        System.out.println("Cards left in the deck: " + deck.deck.size());
        for (int i = 0; i < hand.getHand().size(); i++) {
            System.out.println(hand.getHand().get(i));
        }
    }

    private void processInput() {
        System.out.println("Select cards to change");
        System.out.println("""
                Expected output: X - end game, S - list best scores, C - all comments, R - game rating, Confirm - Confirm
                Example: 1 - select card 1 to change""");
        System.out.println("CURRENT SCORE IS: " + score);
        String playerInput = readLine();
        if (playerInput == null) {
            System.out.println("Nespravny vstup");
            processInput();
            return;
        }
        playerInput = playerInput.trim().toUpperCase();

        if (playerInput.equals("X")) {
            System.out.println("Closing the game.");
            System.exit(0);
        }

        try {
            handleServices(playerInput);
            System.out.println("handleServices");

        } catch (Exception e) {
            System.out.println("handleServices error");
            //empty on purpose
        }

        // overi format vstupu - exception handling
        try {
            System.out.println("handleInput");
            boolean shouldContinue = handleInput(playerInput);
            if (shouldContinue) {
                update();
                processInput();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.out.println("Please repeat your entry.");
            processInput();
        }
    }

    private void handleServices(String playerInput) {
        switch (playerInput) {
            case "S" -> printScores();
            case "C", "R" -> System.out.println("Operation not yet supported.");
        }
    }

    private boolean handleInput(String playerInput) {
        System.out.println(playerInput);
        Pattern pattern = Pattern.compile("[1-5]");
        Matcher matcher = pattern.matcher(playerInput);

        if (matcher.matches()) {
            int num = Integer.parseInt(playerInput);
            if (!selected.contains(num)) {
                System.out.println("Matches");
                selected.add(Integer.parseInt(playerInput));
            }
        }
        if (playerInput.equals("CONFIRM")) {
            for (Integer integer : selected) {
                System.out.println(integer);
                hand.getHand().remove(hand.getHand().get(integer-1));
                for (int i = 0; i < selected.size(); i++) {
                    selected.set(i, selected.get(i)-1);
                }
            }
            hand.draw(deck, selected.size());
            selected.clear();
            logic.setDuplicates(hand);
            score += logic.calculateScore();
            hand.getHand().clear();
            hand.draw(deck, 5);
            return false;

        }
        return true;


    }


    private void printScores() {
        try {
            List<Score> scores = scoreService.getTopScores("mines");
            System.out.println("MINES - TOP SCORES:");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("%10s\t%10s\t%5s\t%s%n", "Game", "Player", "Points", "Date");
            scores.forEach(System.out::println);
            System.out.println("--------------------------------------------------------------");
        } catch (ScoreException e) {
            System.out.println("Problem getting scores from the database.");
        }
    }
}

package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.poker.core.Deck;
import sk.tuke.gamestudio.client.game.poker.core.Hand;
import sk.tuke.gamestudio.client.game.poker.core.Logic;
import sk.tuke.gamestudio.client.game.poker.core.PokerGameState;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.CommentService;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("poker")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PokerController {
    public Hand hand = new Hand();
    private Deck deck = null;
    private List<Integer> selected = new ArrayList<>();
    private PokerGameState state = PokerGameState.DEALT;

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public List<Integer> getSelected() {
        return selected;
    }

    public void setSelected(List<Integer> selected) {
        this.selected = selected;
    }

    public PokerGameState getState() {
        return state;
    }

    public void setState(PokerGameState state) {
        this.state = state;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    private Logic logic = new Logic();
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired UserController userController;

    public List<Comment> getComments() {
        return commentService.getComments("poker");
    }

    public double getAvgRating() {
        return ratingService.getAverageRating("poker");
    }

    public List<Score> getTopScores() {
        return scoreService.getTopScores("poker");
    }

    @RequestMapping
    public String selectCard(@RequestParam(required = false) Integer i) {
        startOrUpdateGame(i);
        return "poker";

    }

    @RequestMapping("/setrating")
    public String setRating(@RequestParam String name, @RequestParam String rating) {
        int value = Integer.parseInt(rating);
        Rating ratingToAdd = new Rating("poker", name, value);
        ratingService.setRating(ratingToAdd);
        return "poker";
    }

    @RequestMapping("/comment")
    public String addComment(@RequestParam String name, @RequestParam String comment) {
        Comment commentToAdd = new Comment();
        commentToAdd.setComment(comment);
        commentToAdd.setPlayer(name);
        commentToAdd.setGame("poker");
        java.sql.Timestamp ts = new Timestamp(System.currentTimeMillis());
        commentToAdd.setCommentedOn(ts);
        commentService.addComment(commentToAdd);
        return "poker";
    }


    private void startOrUpdateGame(Integer i) {
        startNewGameIfDeckNull();
        if (i != null) {
            boolean isCardSelected = hand.getHand().get(i).isSelected();
            hand.getHand().get(i).setSelected(!isCardSelected);

            if (selected.contains(i)) {
                selected.remove(i);
            } else {
                selected.add(i);
            }
        }

    }

    private void startNewGameIfDeckNull() {
        if (deck == null) {
            startNewGame();
        }
    }

    @RequestMapping("/confirm")
    public String confirmGame() {
        state = PokerGameState.CARDS_SWAPPED;
        startNewGameIfDeckNull();
        selected.sort(null);
        for (int i = selected.size() - 1; i >= 0; i--) {
            int num = selected.get(i);
            deck.deck.add(hand.getHand().get(num));
            hand.getHand().remove(num);

            hand.draw(deck, 1);
        }
        logic.setDuplicates(hand);
        logic.setHand(hand.getHand());
        score += logic.calculateScore();


        selected.clear();

        return "poker";
    }

    @RequestMapping("/continue")
    public String continueGame() {
        startNewGameIfDeckNull();
        state = PokerGameState.DEALT;
        deck.deck.addAll(hand.getHand());
        hand.getHand().clear();
        hand.draw(deck, 5);
        return "poker";
    }

    @RequestMapping("/finish")
    private String finishGame() {

        String username = userController.getLoggedUser();
        if(username == null){
            username = "Anonymous user";
        }
        state = PokerGameState.FINISHED;
        startNewGameIfDeckNull();
        deck = null;
        hand = null;
        logic = null;
        if (score > 0) {
            Score dbScore = new Score("poker", username, score, new Date());
            scoreService.addScore(dbScore);
        }
        return "poker";
    }

    private void startNewGame() {
        score = 0;
        selected.clear();
        deck = new Deck();
        hand = new Hand();
        logic = new Logic();
        hand.draw(deck, 5);
        state = PokerGameState.DEALT;

    }

    @RequestMapping("/new")
    public String newGame() {
        startNewGame();
        return "poker";
    }
    /*public String getHtmlField(){
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='pokerhand'> ");
        sb.append("<div>");
        for (int i = 0; i < 5; i++) {
            sb.append("<a href='/poker?i="+i+"' class='pokerlink'>");
            var card = hand.getHand().get(i);
            sb.append(card.toString());
            sb.append("<img src='" + card.getPath() +"' alt='karta'>");
            sb.append("</a>");
        }
        sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }*/
}

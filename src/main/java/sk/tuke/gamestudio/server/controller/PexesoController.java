package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.pexeso.core.Board;
import sk.tuke.gamestudio.client.game.pexeso.core.Card;
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
@RequestMapping("/pexeso")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PexesoController {

    private Board board = null;
    @Autowired
    UserController userController;
    @Autowired
    ScoreService scoreService;
    @Autowired
    RatingService ratingService;
    @Autowired
    CommentService commentService;

    private List<Card> flippedCards = new ArrayList<>();

    private int score = 0;

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        startOrUpdateGame(row, column);
        return "pexeso";
    }

    @RequestMapping("/new")
    public String newGame() {
            startNewGame();
        score = 0;
        return "pexeso";
    }

    private void startOrUpdateGame(Integer row, Integer column) {

        if (board == null) {
            startNewGame();
        }
        if (row != null && column != null) {
            if (!board.isSolved()) {
                if (flippedCards.size() == 2) {
                    Card firstCard = flippedCards.get(0);
                    Card secondCard = flippedCards.get(1);
                    if (firstCard.getValue() == secondCard.getValue()) {
                        //nothing is supposed to happen
                    } else {
                        firstCard.flip();
                        secondCard.flip();
                        score++;
                    }
                    flippedCards.clear();
                }
                Card currentCard = board.getCard(row, column);
                currentCard.flip();
                flippedCards.add(currentCard);
                if (userController.isLogged() && board.isSolved()) {
                    scoreService.addScore(new Score("pexeso", userController.getLoggedUser(), score, new Timestamp(System.currentTimeMillis())));
                }
            }
        }
    }


    private void startNewGame() {
        board = new Board(6, 5);
    }

    @PostMapping
    public String flipCard(@RequestParam int x, @RequestParam int y, Model model) {

        board.flipCard(x, y);
        model.addAttribute("board", board);
        if (board.isSolved()) {
            return "pexeso";
        }
        return "pexeso";
    }
    public List<Score> getTopScores() {
        return scoreService.getTopScores("pexeso");
    }

    public double getRating() {
        return ratingService.getAverageRating("pexeso");
    }
    @PostMapping("/submitComment")
    public String submitComment(@RequestParam("commentText") String commentText) {
        if (userController.isLogged()) {
            commentService.addComment(new Comment(commentText, "pexeso", userController.getLoggedUser(), new Timestamp(System.currentTimeMillis())));
        }
        return "redirect:/pexeso";
    }
    @PostMapping("/submitRating")
    public String submitRating(@RequestParam("rating") int rating) {
        if (userController.isLogged()) {
            ratingService.setRating(new Rating("pexeso",userController.getLoggedUser(),rating));
        }
        return "redirect:/pexeso";
    }
    public List<Comment> getComments() {
        return commentService.getComments("pexeso");

    }
    public Board getBoard() {
        return board;
    }

    public Card[][] getCards() {
        return board.getCards();
    }

    public boolean isSolved() {
        if (board == null) {
            return false;
        } else {
            return (board.isSolved());
        }
    }

    public String getCardText(Card card) {
        if (card.isFlipped()) {
            return card.toString();
        }
        return "X";

    }

    public int getScore() {
        return score;
    }
}
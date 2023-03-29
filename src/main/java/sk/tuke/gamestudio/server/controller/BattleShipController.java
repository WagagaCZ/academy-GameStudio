package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.battleship.core.GameState;
import sk.tuke.gamestudio.client.game.battleship.core.SeaField;
import sk.tuke.gamestudio.client.game.battleship.core.Ship;
import sk.tuke.gamestudio.client.game.battleship.core.Tile;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.CommentService;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreService;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ship")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BattleShipController {
    private SeaField seaField = null;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserController userController;

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        startOrUpdateGame(row, column);
        return "battleship";
    }

    @RequestMapping("/new")
    public String newGame() {
        startNewGame();
        return "battleship";
    }

    private void startOrUpdateGame(Integer row, Integer column) {
        if (seaField == null) {
            startNewGame();
        }
        if (row != null && column != null) {
            GameState stateBeforeMove = seaField.getState();
            if (stateBeforeMove == GameState.PLAYING) {
                seaField.openTile(row, column);
                if (seaField.getState() == GameState.WON && stateBeforeMove != seaField.getState()) {
                    if (userController.isLogged()) {
                        scoreService.addScore(new Score("battleship", userController.getLoggedUser(), seaField.getNumberOfTries(), new Timestamp(System.currentTimeMillis())));
                        return;
                    }
                    return;
                }
                seaField.opponentOpenTile();
            }
        }
    }

    private void startNewGame() {
        seaField = new SeaField();
    }

    public List<Score> getTopScores() {
        return scoreService.getTopScores("battleship");
    }

    public List<Comment> showComments() {
        return commentService.getComments("battleship");
    }

    @PostMapping("/submitComment")
    public String submitComment(@RequestParam("commentText") String commentText) {
        if (userController.isLogged()) {
            commentService.addComment(new Comment(userController.getLoggedUser(), "battleship", commentText, new Timestamp(System.currentTimeMillis())));
        }
        return "redirect:/ship";
    }

    @PostMapping("/rating")
    public String addRating(@RequestParam(name = "ratingValue", required = false) Integer ratingValue,
                            @RequestParam(name = "submitRating", required = false) String submitRating,
                            @RequestParam(name = "getRating", required = false) String getRating,
                            @RequestParam(name = "getAverageRating", required = false) String getAverageRating,
                            Model model) {
        if (submitRating != null && ratingValue != null && userController.isLogged()) {
            ratingService.setRating(new Rating("battleship", userController.getLoggedUser(), ratingValue));
        } else if (getRating != null && userController.isLogged()) {
            Integer rating = ratingService.getRating("battleship", userController.getLoggedUser());
            model.addAttribute("ratingValue", rating);
        } else if (getAverageRating != null && userController.isLogged()) {
            Integer averageRating = ratingService.getAverageRating("battleship");
            model.addAttribute("ratingValue", averageRating);
        }
        return "battleship";
    }

    public Tile[][] getPlayerFieldTiles() {
        if (seaField == null) {
            return null;
        } else {
            return seaField.getPlayerTiles();
        }
    }

    public Tile[][] getEnemyFieldTiles() {
        if (seaField == null) {
            return null;
        } else {
            return seaField.getEnemyTiles();
        }
    }

    public boolean isPlaying() {
        if (seaField == null) {
            return false;
        } else {
            return (seaField.getState() == GameState.PLAYING);
        }
    }

    public String getTileText(Tile tile) {
        switch (tile.getState()) {
            case WATER -> {
                return "~";
            }
            case MISS -> {
                return "O";
            }
            case HIT -> {
                return "X";
            }
            default -> {
                if (tile instanceof Ship) {
                    return "#";
                } else {
                    return "~";
                }
            }
        }
    }


    public String getTileClass(Tile tile) {
        if(seaField.getState() == GameState.WON) {
            if (tile.getState() == Tile.State.WATER) {
                return "water-endgame";
            }
        } else if(seaField.getState() == GameState.FAILED) {
            if(tile.getState() == Tile.State.WATER && tile instanceof Ship) {
                return "ship";
            } else {
                switch (tile.getState()) {
                    case WATER -> {
                        return "water-endgame";
                    }
                    case MISS -> {
                        return "missed";
                    }
                    case HIT -> {
                        return "hit";
                    }
                    default -> throw new RuntimeException("Unexpected tile state");
                }
            }
        }
        switch (tile.getState()) {
            case WATER -> {
                return "water";
            }
            case MISS -> {
                return "missed";
            }
            case HIT -> {
                return "hit";
            }
            default -> throw new RuntimeException("Unexpected tile state");
        }
    }

    public String getTileClassPlayer(Tile tile) {
        switch (tile.getState()) {
            case WATER -> {
                if (tile instanceof Ship) {
                    return "ship";
                } else {
                    return "water-player";
                }
            }
            case MISS -> {
                return "missed";
            }
            case HIT -> {
                return "hit";
            }
            default -> throw new RuntimeException("Unexpected tile state");
        }
    }

    public String getNumberOfPlayerTiles() {
        return Integer.toString(seaField.getNumberOfPlayerShip());
    }

    public String getNumberOfEnemyTiles() {
        return Integer.toString(seaField.getNumberOfEnemyShip());
    }



}


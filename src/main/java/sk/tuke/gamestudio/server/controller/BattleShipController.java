package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

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
                        scoreService.addScore(new Score("battleship", userController.getLoggedUser(), seaField.getNumberOfTries(), Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(new Date().getTime())))));
                        return;
                    }
                    return;
                }
                seaField.opponentOpenTile();
            }
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Void> startNewGame() {
        seaField = new SeaField();
        return ResponseEntity.ok().build();
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
            commentService.addComment(new Comment(userController.getLoggedUser(), "battleship", commentText, Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(new Date().getTime())))));
        }
        return "redirect:/ship";
    }

    @GetMapping("/rating")
    @ResponseBody
    public Map<String, Object> getRating(@RequestParam(name = "getRating", required = false) String getRating) {
        Map<String, Object> responseData = new HashMap<>();
        if (getRating != null && userController.isLogged()) {
            Integer rating = ratingService.getRating("battleship", userController.getLoggedUser());
            responseData.put("ratingValue", rating);
        }
        return responseData;
    }

    @GetMapping("/average-rating")
    @ResponseBody
    public Map<String, Object> getAverageRating(@RequestParam(name = "getAverageRating", required = false) String getAverageRating) {
        Map<String, Object> responseData = new HashMap<>();
        if (getAverageRating != null && userController.isLogged()) {
            Integer averageRating = ratingService.getAverageRating("battleship");
            responseData.put("averageRating", averageRating);
        }
        return responseData;
    }

    @PostMapping("/rating")
    public String addRating(@RequestParam(name = "ratingValue", required = false) Integer ratingValue,
                            @RequestParam(name = "submitRating", required = false) String submitRating,
                            Model model) {
        if (ratingValue != null && userController.isLogged()) {
            ratingService.setRating(new Rating("battleship", userController.getLoggedUser(), ratingValue));
        }
        return "redirect:/ship#rating-form";
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
        if (seaField.getState() == GameState.WON) {
            if (tile.getState() == Tile.State.WATER) {
                return "water-endgame";
            }
        } else if (seaField.getState() == GameState.FAILED) {
            if (tile.getState() == Tile.State.WATER && tile instanceof Ship) {
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


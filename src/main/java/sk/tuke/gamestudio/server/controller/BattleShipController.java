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
    public String processUserInput(@RequestParam(required = false) Integer row,
                                   @RequestParam(required = false) Integer column) {
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
                        return; //return sa spravi tak ci tak po tomto if-e, naco ti je tu potom tento tu?
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
        return scoreService.getTopScores("battleship"); //"battleship" pouzivas velakrat, oplati sa spravit si na to konstantu -> refactor -> Introduce Constant
    }


    public List<Comment> showComments() { //toto nie je show comments, to je skor get alebo load
        return commentService.getComments("battleship");
    }

    @PostMapping("/submitComment")
    public String submitComment(@RequestParam("commentText") String commentText) {
        if (userController.isLogged()) {
            //ak mas taketo dlhe riadky, tak to odenteruj, kazdy parameter na novy riadok
            commentService.addComment(new Comment(userController.getLoggedUser(),
                    "battleship", commentText,
                    //toto vytvorenie timestamp tu opakujes viackrat, treba to zrefaktorovat a hodit do privatnej metody (Refactor -> Extract Method)
                    //ale aj tak si myslim, ze nejake moc komplikovane to je na jednoduchy Timestamp...
                    Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(new Date().getTime())))));
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
        return seaField != null ? seaField.getPlayerTiles() : null;
    }

    public Tile[][] getEnemyFieldTiles() {
        return seaField != null ? seaField.getEnemyTiles() : null; //ak tu nahodou vratis null, nepadne ti to na strane klienta? nemal by si tu vratit aspon nejake nove prazdne pole docasne?
    }

    public boolean isPlaying() {
        return seaField != null && seaField.getState() == GameState.PLAYING;
    }

    public String getTileText(Tile tile) {
        //takychto switchov tu mas viacej, da sa to takto prepisat
        return switch(tile.getState()) {
            case WATER -> "~";
            case MISS -> "0";
            case HIT -> "X";
            default -> tile instanceof Ship ? "#" : "~";
        };
    }


    public String getTileClass(Tile tile) {
        if (seaField.getState() == GameState.WON &&
                tile.getState() == Tile.State.WATER) {
            return "water-endgame";
        } //ak mas return, netreba ti else, podobne aj nizsie

        if (seaField.getState() == GameState.FAILED) {
            if (tile.getState() == Tile.State.WATER && tile instanceof Ship) {
                return "ship";
            }

            return switch (tile.getState()) {
                case WATER -> "water-endgame";
                case MISS -> "missed";
                case HIT -> "hit";
                default -> throw new RuntimeException("Unexpected tile state");
            };
        }

        return switch (tile.getState()) {
            case WATER -> "water";
            case MISS -> "missed";
            case HIT -> "hit";
            default -> throw new RuntimeException("Unexpected tile state");
        };
    }

    public String getTileClassPlayer(Tile tile) {
        return switch (tile.getState()) {
            case WATER -> tile instanceof Ship ? "ship" : "water-player";
            case MISS -> "missed";
            case HIT -> "hit";
            default -> throw new RuntimeException("Unexpected tile state");
        };
    }

    public String getNumberOfPlayerTiles() {
        return Integer.toString(seaField.getNumberOfPlayerShip());
    }

    public String getNumberOfEnemyTiles() {
        return Integer.toString(seaField.getNumberOfEnemyShip());
    }
}


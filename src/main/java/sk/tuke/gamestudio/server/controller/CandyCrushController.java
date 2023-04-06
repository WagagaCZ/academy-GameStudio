package sk.tuke.gamestudio.server.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.client.game.candycrush.*;
import sk.tuke.gamestudio.client.game.candycrush.core.Field;
import sk.tuke.gamestudio.client.game.candycrush.core.GameState;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreService;

@Controller
@RequestMapping("/candycrush")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CandyCrushController {

    private CandyCrush candyCrush = null;

    private Field field = null;
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserController userController;


    // /candycrush
    @RequestMapping
    public String index() {
        startNewGame();
        return "candycrush";
    }

    // /candycrush/new
    @RequestMapping("/new")
    public String startNewGame() {
        field = new Field(9,9);
        return "candycrush";
    }

    // /matchthree/move
    @RequestMapping("/move")
    @ResponseBody
    public String move(@RequestParam int x1, @RequestParam int y1, @RequestParam int x2, @RequestParam int y2) {
        startOrUpdateGame(x1,y2,x2,y2);
        if (field.isSolved()) {
            if (userController.isLogged()) {
                scoreService.addScore(new Score("matchthree", userController.getLoggedUser(), field.getScore(), new Date()));
            }
        }
        return "candycrush";
    }

    // /matchthree/json
    @RequestMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field proccessUserInput(@RequestParam(required = false) Integer row1, @RequestParam(required = false) Integer column1, @RequestParam(required = false) Integer row2, @RequestParam(required = false) Integer column2) {
        field.setJustFinished(startOrUpdateGame(row1,column1,row2,column2));
        return field;
    }

    @RequestMapping(value="/jsonnew", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field newGameJson(){
        startNewGame();
        field.setJustFinished(false);
        return field;
    }
    @RequestMapping(value = "/api/gamefield", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field getGameField() {
        if (field == null) {
            startNewGame();
        }
        return field;
    }

//    public CandyCrush getCandyCrush() {
//        return candyCrush;

//    }

    private boolean startOrUpdateGame(Integer row1, Integer column1, Integer row2, Integer column2) {
        if (candyCrush == null) {
            startNewGame();
        }

        boolean justFinished=false;

        if (row1 != null && column1 != null && row2 != null && column2 != null) {
            if (isValidSwap(row1, column1, row2, column2)) {
                move(row1, column1, row2, column2);
                if (field.hasThreeInARow()) {
                    field.removeMatches();
                }
            } else {
                return false;
            }

            if (field.isSolved()) {
                if (userController.isLogged()) {
                    scoreService.addScore(new Score("matchthree", userController.getLoggedUser(), field.getScore(), new Date()));
                }
                return true;
            }
        }

        return justFinished;
    }

    public boolean isValidSwap(int row1, int col1, int row2, int col2) {
        // The two tiles must be adjacent to each other
        if (!isAdjacent(row1, col1, row2, col2)) {
            return false;
        }

        // Swap the tiles temporarily and check if the swap results in a match
        move(row1, col1, row2, col2);
        boolean hasMatch = field.hasThreeInARow();
        move(row1, col1, row2, col2); // Swap back to the original state

        return hasMatch;
    }

    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1;
    }


    public boolean isPlaying() {
        if (field == null) {
            return false;
        } else {
            return !field.isSolved();
        }
    }

    public int getScore() {
        if (field == null) {
            return 0;
        } else {
            return field.getScore();
        }
    }

    public List<Score> getTopScores() {
        return scoreService.getTopScores("matchthree");
    }
}

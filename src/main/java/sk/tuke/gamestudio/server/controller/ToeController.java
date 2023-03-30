package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.tiktaktoe.StateTile;
import sk.tuke.gamestudio.client.game.tiktaktoe.TiktaktoeWebField;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.RatingException;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreException;
import sk.tuke.gamestudio.common.service.ScoreService;
import sk.tuke.gamestudio.server.EntityDTO.RatingDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static sk.tuke.gamestudio.common.Constants.*;

@Controller
@RequestMapping("/toe")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ToeController {
    @Autowired
    ScoreController scoreController;
    @Autowired
    ScoreService scoreService;

    @Autowired
    RatingService ratingService;

    TiktaktoeWebField field = new TiktaktoeWebField();
    public static final String userName = System.getProperty("user.name");

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    private String gameResult = WIN;
    public boolean gameOver = false;
    public static long startMillis;
    int countOfPlayerMove = 0;

    @RequestMapping
    public String processInput(@RequestParam(required = false) Integer index) throws InterruptedException {
        if (index != null) {
            updateGame(index);
        }
        return "toe.html";
    }

    @GetMapping("toe")
    public String index(Model model) throws ScoreException, RatingException {
        model.addAttribute("score", new Score());
        List<Score> list = scoreService.getTopScores(TIC_TAC_TOE);
        model.addAttribute("list", list);
        System.out.println(list);
        return "toe.html";
    }

    @RequestMapping("/new")
    public String newGame() {
        startNewGame();
        return "toe";
    }

    @GetMapping("/score")
    public String greeting(Model model) throws ScoreException, RatingException {


        model.addAttribute("rating", new Rating());
        double ratingToe = ratingService.getAverageRating(TIC_TAC_TOE);
        model.addAttribute("ratingToe", ratingToe);
        return "toe";
    }

    @PostMapping(value = "/addrating")
    public String addComment(@ModelAttribute("rating") RatingDTO rating) throws RatingException {

        if (rating != null) {
            Rating ratingForSave = new Rating();
            if (rating.getGame().equals("MINESWEEPER")) {
                ratingForSave.setGame(MINESWEEPER);
            } else if (rating.getGame().equals("PUZZLE_FIFTEEN")) {
                ratingForSave.setGame(PUZZLE_FIFTEEN);
            } else if (rating.getGame().equals("TIC_TAC_TOE")) {
                ratingForSave.setGame(TIC_TAC_TOE);
            }
            ratingForSave.setRating(rating.getRating());
            ratingForSave.setUsername(userName);
            ratingForSave.setRatedAt(Timestamp.valueOf(LocalDateTime.now()));
            ratingService.setRating(ratingForSave);
        }
        return "toe";
    }

    public String getToeField() {
        List<StateTile> tileList = field.getData();
        int index = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='toe'> \n");
        for (int row = 0; row < 3; row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < 3; column++) {
                sb.append("<td>\n");
                sb.append("<a href='/toe?index=").append(index).append("'>\n");
                sb.append("<span>").append(tileList.get(index).getStateTile()).append("</span>");
                index++;
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private void startNewGame() {
        field = new TiktaktoeWebField();
        startMillis = System.currentTimeMillis();
    }

    private void updateGame(int index) throws InterruptedException {
        //ak je dlazdica prazdna
        if (field.getTikTakToeField().get(index) == StateTile.EMPTY) {
            //dame krizek
            field.getTikTakToeField().put(index, StateTile.CROSS);
            countOfPlayerMove++;
            //skontrolujeme vyhru
            if (checkWin()) {
                setScore();
                setGameResult(WIN);
                return;
            }

            //chodi PC - prvu prazdnu dlazdicu
            int zeroIndex = field.getIndexEmptyTile();
            if (zeroIndex >= 0) {
                Thread.sleep(100);
                field.getTikTakToeField().put(zeroIndex, StateTile.ZERO);
                if (checkWin()) {
                    setGameResult(GAME_OVER);
                    return;
                }
            }
            //kontrola - remiza
            else {
                gameOver = true;
                setGameResult(REMIZA);
                return;
            }
        }
    }

    public boolean checkWin() {
        StateTile winner = field.checkWin();
        if (StateTile.CROSS == winner || StateTile.ZERO == winner) {
            List<StateTile> data = field.getData();
            return true;
        }
        return false;
    }

    public String getTime() {
        long gameTime = (System.currentTimeMillis() - startMillis) / 1000;
        return String.valueOf(gameTime);
    }

    public void setScore() {
        try {
            Score score = new Score();
            score.setPlayer(userName);
            score.setGame(TIC_TAC_TOE);
            //todo treba naopak
            score.setPoints(countOfPlayerMove);
            score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
            scoreController.postScore(score);
        } catch (Exception e) {
            System.out.println("Couldn't save your score, check database connection.");
        }
    }

}



package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.tiktaktoe.TileState;
import sk.tuke.gamestudio.client.game.tiktaktoe.TikTakToeWebField;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.*;
import sk.tuke.gamestudio.server.service.RatingServiceJPA;
import sk.tuke.gamestudio.server.service.ScoreServiceJPA;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Formatter;
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
    @Autowired
    RatingServiceJPA ratingServiceJPA; //preco tu mas toto JPA, ked uz mas wirenute vsetky servisy cez rozhranie? nerozumiem
    @Autowired
    ScoreServiceJPA scoreServiceJPA;
    @Autowired
    CommentService commentService;
    TikTakToeWebField field = new TikTakToeWebField();
    public static final String userName = System.getProperty("user.name");
    private String gameResult = WIN;
    public boolean gameOver = false;
    public static long startMillis;
    int countOfPlayerMove = 0;

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    @RequestMapping
    public String processInput(@RequestParam(required = false) Integer index) throws InterruptedException {
        if (index != null) {
            updateGame(index);
        }
        return "toe";
    }

    @GetMapping("/toe")
    public String index(Model model) throws ScoreException, RatingException {
        return "toe";
    }

    @RequestMapping("/new")
    public String newGame() {
        startNewGame();
        return "redirect:/toe";
    }

    @PostMapping("/rating")
    public String addRating(@RequestParam(name = "ratingValue", required = false) Integer ratingValue,
                            Model model) {
        if (ratingValue != null) {
            ratingService.setRating(new Rating(TIC_TAC_TOE, userName, ratingValue));
        }
        return "toe";
    }

    @PostMapping(value = "/comments")
    public String addComment(@RequestParam(name = "commentText", required = false) String commentText,
                             Model model) throws CommentException {
        if (commentText != null) {
            commentService.addComment(new Comment(userName, TIC_TAC_TOE, commentText, new Timestamp(System.currentTimeMillis())));
        }
        return "toe";
    }

    public String getToeField() { //je to len moja konvencia, ale primarne vsetko, co je spojene s vykreslovanim, pouziva render(ed), cize ja by som to nazvala getRenderedToeField
        List<TileState> tileList = field.getData();
        int index = 0;
        Formatter f = new Formatter();

        f.format("<table class='toe'> \n");
        for (int row = 0; row < 3; row++) {
            f.format("<tr>\n");
            for (int column = 0; column < 3; column++) {
                f.format("""
                        <td>
                          <a href='toe?index=%s'>
                            <span> %s </span>
                          </a>
                        </td>
                        """, index, tileList.get(index).getStateTile());
                index++;
//                f.format("<td>\n");
//                f.format("<a href='/toe?index=").format(index).format("'>\n");
//                f.format("<span>").format(tileList.get(index).getStateTile()).format("</span>");
//                index++;
//                f.format("</a>\n");
//                f.format("</td>\n");
            }
            f.format("</tr>\n");
        }
        f.format("</table>\n");
        return f.toString();
    }

    private void startNewGame() {
        field = new TikTakToeWebField();
        startMillis = System.currentTimeMillis();
    }

    private void updateGame(int index) throws InterruptedException {
        //ak je dlazdica prazdna
        if (field.getTikTakToeField().get(index) == TileState.EMPTY) {
            //dame krizek
            field.getTikTakToeField().put(index, TileState.CROSS);
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
                field.getTikTakToeField().put(zeroIndex, TileState.ZERO);
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
        TileState winner = field.checkWin();
        if (TileState.CROSS == winner || TileState.ZERO == winner) {
            List<TileState> data = field.getData();
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
            score.setPoints(countOfPlayerMove * (-1));
            score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
            scoreController.postScore(score);
        } catch (Exception e) {
            System.out.println("Couldn't save your score, check database connection.");
        }
    }

    public List<Score> getTopScores() {
        List<Score> list;
        list = scoreService.getTopScores(TIC_TAC_TOE);
        if (!list.isEmpty()) {
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    public int getAvverageRating() {
        return ratingService.getAverageRating(TIC_TAC_TOE);
    }

    public List<Comment> getToeComments() {
        return commentService.getComments(TIC_TAC_TOE);
    }
}



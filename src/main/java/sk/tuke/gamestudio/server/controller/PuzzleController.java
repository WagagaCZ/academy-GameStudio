package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.puzzle.PuzzleFieldInit;
import sk.tuke.gamestudio.client.game.puzzle.PuzzleFifteenLogic;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.CommentException;
import sk.tuke.gamestudio.common.service.CommentService;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static sk.tuke.gamestudio.common.Constants.*;
import static sk.tuke.gamestudio.server.controller.ToeController.userName;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/puzzle")
public class PuzzleController implements PuzzleFieldInit {
    private final PuzzleFifteenLogic puzzleFifteenLogic = new PuzzleFifteenLogic();
    @Autowired
    ScoreController scoreController;
    @Autowired
    ScoreService scoreService;
    @Autowired
    RatingService ratingService;
    @Autowired
    CommentService commentService;
    static int size = 4;
    static int[][] grid;
    List<Integer> winCombination = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0);
    private String gameResult = WIN;
    public boolean checkWin = false;
    public int countOfMove = 0;
    private static final String SAVED_GAME = System.getProperty("user.home") + System.getProperty("file.separator") + "saved.game";
    public boolean isSaved = false;
    public boolean isLoaded = false;

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer index) {
        isSaved = false;
        isLoaded = false;
        startOrUpdateGame(index);
        return "puzzle";
    }

    @RequestMapping("/new")
    public String newGame() {
        isSaved = false;
        isLoaded = false;
        startNewGame();
        return "redirect:/puzzle";
    }

    @RequestMapping("/save")
    public String saveGame() {
        isSaved = false;
        isLoaded = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(SAVED_GAME);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(grid);
            objectOutputStream.writeObject(countOfMove);
            isSaved = true;
        } catch (IOException e) {
            isSaved = false;
            System.out.println("Game was not saved" + e.getMessage());
        } finally {
            return "redirect:/puzzle";
        }
    }

    @RequestMapping("/load")
        public String getSavedGame() throws IOException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVED_GAME));
        isSaved = false;
        isLoaded = false;
        try {
            grid = (int[][]) in.readObject();
            isLoaded = true;
            countOfMove = (int) in.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Saved game not exists");
            isLoaded = false;
            startNewGame();
        } finally {
            return "redirect:/puzzle";
        }
    }

    @PostMapping("/ratings")
    public String addRating(@RequestParam(name = "ratingValue", required = false) Integer ratingValue,
                            Model model) {
        if (ratingValue != null) {
            ratingService.setRating(new Rating(PUZZLE_FIFTEEN, userName, ratingValue));
        }
        return "puzzle";
    }

    @PostMapping(value = "/comments")
    public String addComment(@RequestParam(name = "commentText", required = false) String commentText,
                             Model model) throws CommentException {
        if (commentText != null) {
            commentService.addComment(new Comment(userName, PUZZLE_FIFTEEN, commentText, new Timestamp(System.currentTimeMillis())));
        }
        return "puzzle";
    }

    public String getPuzzleField() {
        if (grid == null) {
            startNewGame();
        }

        int index = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='puzzle'> \n");

        for (int row = 0; row < size; row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < size; column++) {
                sb.append("<td>\n");
                sb.append("<a href='/puzzle?index=" + index + "'>\n");
                index++;
                if (grid[row][column] == 0) {
                    sb.append("<span>" + " " + "</span>");
                } else {
                    sb.append("<span>" + grid[row][column] + "</span>");
                }
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }


    void startNewGame() {
        grid = new int[size][size];
        countOfMove = 0;

        PuzzleFieldInit.baseArrayInit(grid);
        do {
            PuzzleFieldInit.initRandomTiles(grid);
        }
        while (PuzzleFieldInit.isSolved(grid));
    }

    void startOrUpdateGame(Integer index) {
        int temp = 0;
        int listIndex = 0;
        boolean isMove = false;
        if (grid == null) {
            startNewGame();
        }
        //if parameter was input
        if (index != null) {
            List<Integer> list = Arrays.stream(grid).flatMap(row -> Arrays.stream(row).boxed()).collect(Collectors.toList());
            if (!Objects.equals(list, winCombination)) {
                //moveleft
                if ((index - 1) >= 0 && (index - 1) <= 15) {
                    if (list.get(index - 1) == 0) {
                        if (puzzleFifteenLogic.isCanMoveLeft(index)) {
                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index - 1, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }
                //moveright
                if ((index + 1) >= 0 && (index + 1) <= 15) {
                    if (list.get(index + 1) == 0) {
                        if (puzzleFifteenLogic.isCanMoveRight(index)) {

                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index + 1, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }
                //movedown
                if ((index + 4) >= 0 && (index + 4) <= 15) {
                    if (list.get(index + 4) == 0) {
                        if (puzzleFifteenLogic.isCanMoveDown(index)) {

                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index + 4, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }
                //moveup
                if ((index - 4) >= 0 && (index - 4) <= 15) {
                    if (list.get(index - 4) == 0) {
                        if (puzzleFifteenLogic.isCanMoveUp(index)) {
                            temp = list.get(index);
                            list.set(index, 0);
                            list.set(index - 4, temp);
                            temp = 0;
                            isMove = true;
                        }
                    }
                }

            }

            if (isMove) {
                countOfMove++;
                for (int r = 0; r < 4; r++) {
                    for (int c = 0; c < 4; c++) {
                        grid[r][c] = list.get(listIndex);
                        listIndex++;
                    }
                }
            }

            if (Objects.equals(list, winCombination)) {
                setGameResult(WIN);
                checkWin = true;
                return;
            }
        }
    }

    public int getCountOfMove() {
        return countOfMove;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public void setScore() {
        try {
            Score score = new Score();
            score.setPlayer(userName);
            score.setGame(PUZZLE_FIFTEEN);
            score.setPoints(-1 * countOfMove);
            score.setPlayedOn(Timestamp.valueOf(LocalDateTime.now()));
            scoreController.postScore(score);
        } catch (Exception e) {
            System.out.println("Couldn't save your score, check database connection.");
        }
    }

    public List<Score> getTopScores() {
        return scoreService.getTopScores(PUZZLE_FIFTEEN);
    }

    public int getAvverageRating() {
        return ratingService.getAverageRating(PUZZLE_FIFTEEN);
    }

    public List<Comment> getPuzzleComments() {
        return commentService.getComments(PUZZLE_FIFTEEN);
    }
}



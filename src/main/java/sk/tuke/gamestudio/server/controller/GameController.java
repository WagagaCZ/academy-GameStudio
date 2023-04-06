package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.CommentService;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public abstract class GameController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserController userController;

    protected abstract String getGameName();

    @RequestMapping("/comment")
    public String comment(@RequestParam(required = true) String comment) {
        this.commentService.addComment(new Comment(userController.getLoggedUser(), getGameName(), comment, new Timestamp(System.currentTimeMillis())));
        return getGameName();
    }

    @RequestMapping("/rating")
    public String rating(@RequestParam(required = true) int rating) {
        this.ratingService.setRating(new Rating(getGameName(), userController.getLoggedUser(), rating));
        return getGameName();
    }

    public List<Comment> getComments(){
        return commentService.getComments(getGameName());
    }

    public int getAverageRating(){
        return ratingService.getAverageRating(getGameName());
    }

    public List<Score> getTopScores(){
        return scoreService.getTopScores(getGameName());
    }
}


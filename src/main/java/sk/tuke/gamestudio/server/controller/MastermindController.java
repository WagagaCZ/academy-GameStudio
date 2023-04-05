package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.mastermind.MastermindField;
import sk.tuke.gamestudio.common.service.CommentService;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreService;

@Controller
@RequestMapping("/mastermind2")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MastermindController extends GameController {

    private MastermindField field;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserController userController;



    private void startNewGame() {
        field = new MastermindField();
    }

    @Override
    protected String getGameName() {
        return "Mastermind";
    }
}

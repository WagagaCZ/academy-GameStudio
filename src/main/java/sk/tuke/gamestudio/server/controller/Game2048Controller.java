package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.game2048.core.Direction;
import sk.tuke.gamestudio.client.game.game2048.core.Field;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.util.Date;

@RestController
@RequestMapping("/api/2048")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class Game2048Controller {
    @Autowired
    ScoreService scoreService;
    @Autowired
    UserController userController;
    private Field field;
    boolean lastMoveState;
    @GetMapping("/new")
    public Field newGame(@RequestParam int size) {
        this.field = new Field(size, size);
        // true as the game didn't end last move
        this.lastMoveState = true;
        return this.field;
    }

    @GetMapping("/move")
    public Field makeMove(@RequestParam(name = "dir") Direction direction) {
        boolean currMoveState = this.field.doMove(direction);
        if( !currMoveState ) {
            // if state changed and lastMoveState wasn't false then write score
            if( lastMoveState != currMoveState )
                scoreService.addScore( new Score("2048", userController.isLogged() ? userController.getLoggedUser() : "anonymous", this.field.getScore(), new Date() ) );
            lastMoveState = currMoveState;
        }
        return this.field;
    }
}

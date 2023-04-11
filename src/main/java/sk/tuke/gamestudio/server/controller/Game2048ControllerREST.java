package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.tuke.gamestudio.client.game.game2048.core.Direction;
import sk.tuke.gamestudio.client.game.game2048.core.Field;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.util.Date;

@RestController
@RequestMapping("/api/2048")
public class Game2048ControllerREST {
    private Field field;

    @GetMapping("/new")
    public Field newGame(@RequestParam int size) {
        this.field = new Field(size, size);
        return this.field;
    }

    @GetMapping("/move")
    public Field makeMove(@RequestParam(name = "dir") Direction direction) {
        this.field.doMove(direction);
        return this.field;
    }
}
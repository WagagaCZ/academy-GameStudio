package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreWebServiceREST {
    // enables to create http messages,
    // send them to client and accept responses
    @Autowired
    ScoreService scoreService;

    //POST localhost:8080/api/score
    // objekt bude v tele spravy
    @PostMapping
    public void addScore(@RequestBody Score score) throws ScoreException {
        scoreService.addScore(score);
    }

    //GET localhost:8080/api/score/mines
    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) throws ScoreException {
        return scoreService.getTopScores(game);
    }
}

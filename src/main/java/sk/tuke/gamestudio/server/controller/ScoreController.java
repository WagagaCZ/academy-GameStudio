package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreException;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/top/{game}")
    public ResponseEntity<?> getTopScores(@PathVariable String game) {
        try {
            List<Score> topScores = scoreService.getTopScores(game);
            if(topScores.isEmpty()) {
//                return ResponseEntity.notFound().build();
                return new ResponseEntity<>(Map.of("message","No scores found for game " + game), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(topScores);

        } catch (ScoreException e) {
            return serverError(e);
        }
    }

    @PostMapping({"","/"})
    public ResponseEntity<?> postScore(@RequestBody Score score) {
        try {
            score.setPlayedOn(new Date());
            scoreService.addScore(score);
            return new ResponseEntity<>(Map.of("message","Score added"), HttpStatus.CREATED);
        } catch (ScoreException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    private static ResponseEntity<?> serverError(ScoreException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("message","Server Error"));
    }
}

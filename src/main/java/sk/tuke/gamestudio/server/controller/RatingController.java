package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.service.CommentException;
import sk.tuke.gamestudio.common.service.RatingService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("api/v2/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/avg/{game}") // path param: game:  /api/ratingavg/Blocks
    public ResponseEntity<?> getAverageRating(@PathVariable String game) {
        try {
            double avg = ratingService.getAverageRating(game);
            if (avg == -1) {
                return new ResponseEntity<>(Map.of("message","No ratings found for game " + game), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(avg);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @PostMapping({"","/"})
    public ResponseEntity<?> postRating(@RequestBody Rating rating) {
        try {
            rating.setRatedAt(new Date());
            ratingService.setRating(rating);
            return new ResponseEntity<>(Map.of("message","Rating added"), HttpStatus.CREATED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }
}

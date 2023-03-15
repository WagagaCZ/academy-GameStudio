package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.RatingException;

@RestController
@RequestMapping("/api/rating")
public class RatingWebServiceREST {
    @Autowired
    RatingService ratingService;

    @PostMapping
    void setRating(@RequestBody Rating rating) throws RatingException {
        ratingService.setRating(rating);
    }

    @GetMapping("/{game}")
    int getAverageRating(@PathVariable String game) throws RatingException {
        return ratingService.getAverageRating(game);
    }

//    @GetMapping("/{game}")
//    Rating getAverageRating(@PathVariable String game) throws RatingException {
//        return new Rating(ratingService.getAverageRating(game));
//    }

    @GetMapping("/{game}/{player}")
    int getRating(@PathVariable String game, @PathVariable String player) throws RatingException {
        return ratingService.getRating(game, player);
    }

//    @GetMapping("/{game}/{player}")
//    Rating getRating(@PathVariable String game, @PathVariable String player) throws RatingException {
//        return new Rating(ratingService.getRating(game, player));
//    }
}

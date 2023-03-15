package sk.tuke.gamestudio.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.service.RatingException;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreException;

public class RatingClientServiceREST implements RatingService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${server.rest.service.api}")
    private String URL;


    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(URL + "/rating", rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return restTemplate.getForEntity(URL + "/rating/" + game, Integer.class)
                .getBody();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return restTemplate.getForEntity(URL + "/rating/" + game + "/" + player, Integer.class)
                .getBody();
    }

    @Override
    public void reset() throws ScoreException {
        throw new UnsupportedOperationException("Reset is not supported via web.");
    }
}

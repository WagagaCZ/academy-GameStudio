package sk.tuke.gamestudio.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreException;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.util.Arrays;
import java.util.List;

public class ScoreServiceREST implements ScoreService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${server.rest.service.api}")
    private String URL;

    @Override
    public void addScore(Score score) throws ScoreException {
        restTemplate.postForEntity(URL + "/score", score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
//        return Arrays.asList(
//                restTemplate.getForEntity(URL + "/score/" + game, Score[].class)
//                        .getBody());
        ResponseEntity<List<Score>> response = restTemplate.exchange(
                URL + "/score/" + game,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Score>>() {});
        return response.getBody();
    }

    @Override
    public void reset() throws ScoreException {
        throw new UnsupportedOperationException("Reset is not supported via web.");
    }
}

package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;

//localhost:8080/api/score
public class ScoreServiceREST implements ScoreService {
//    enables to create http messages,
//    send them to server and accept responses
    @Autowired
    RestTemplate restTemplate;

    private static final String URL = "http://localhost:8080/api";

    @Override
    public void addScore(Score score) throws ScoreException {
        restTemplate.postForEntity(URL + "/score", score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        return Arrays.asList(restTemplate
                .getForEntity(URL + "/score/" + game, Score[].class)
                .getBody());
    }

    @Override
    public void reset() {
//        not secure, we will not support it
        throw new UnsupportedOperationException("Reset not supported via web.");
    }
}

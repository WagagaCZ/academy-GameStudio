package sk.tuke.gamestudio.server.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreException;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@Transactional
class ScoreServiceJPATest {

    // testing with h2 in memory database

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScoreServiceJPA scoreService;

    @Test
    void testAddScore() throws ScoreException {
        Score score = new Score("test", "test", 100, new Date(8000000));
        scoreService.addScore(score);
        Score result = entityManager.find(Score.class, score.getId());
        assertEquals(score, result);
    }


    @Test
    void testGetTopScores() throws ScoreException {
        entityManager.persist(new Score("test", "test1", 100, new Date(8000000)));
        entityManager.persist(new Score("test", "test2", 90, new Date(8000000)));
        entityManager.persist(new Score("test", "test3", 80, new Date(8000000)));

        List<Score> expectedScores = new ArrayList<>();
        expectedScores.add(new Score("test", "test1", 100, new Date(8000000)));
        expectedScores.add(new Score("test", "test2", 90, new Date(8000000)));
        expectedScores.add(new Score("test", "test3", 80, new Date(8000000)));

        List<Score> result = scoreService.getTopScores("test");

        expectedScores.sort(Comparator.comparing(Score::getPoints).reversed());
        result.sort(Comparator.comparing(Score::getPoints).reversed());

        assertEquals(expectedScores.size(), result.size(), "Size of lists should be equal");
        for (int i = 0; i < expectedScores.size(); i++) {
            assertEquals(expectedScores.get(i).getGame(), result.get(i).getGame(), "Game should be equal");
            assertEquals(expectedScores.get(i).getPlayer(), result.get(i).getPlayer(), "Player should be equal");
            assertEquals(expectedScores.get(i).getPoints(), result.get(i).getPoints(), "Points should be equal");
            assertEquals(expectedScores.get(i).getPlayedOn(), result.get(i).getPlayedOn(), "PlayedOn should be equal");
        }

    }



}
package sk.tuke.gamestudio.server.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.service.RatingException;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class RatingServiceJPATest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RatingServiceJPA ratingService;



    @Test
    void setRating() throws RatingException {
        Rating newRating = new Rating("test", "test", 5);
        ratingService.setRating(newRating);
        Rating result = entityManager.find(Rating.class, newRating.getIdent());
    }

    @Test
    void getAverageRating() throws RatingException {
        Rating newRating = new Rating("test", "test", 5);
        ratingService.setRating(newRating);
        Rating result = entityManager.find(Rating.class, newRating.getIdent());
        assertEquals(newRating, result);
    }

    @Test
    void getRating() throws RatingException {
        Rating newRating = new Rating("test", "test", 5);
        ratingService.setRating(newRating);
        Rating result = entityManager.find(Rating.class, newRating.getIdent());
        assertEquals(newRating, result);
    }
}
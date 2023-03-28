package sk.tuke.gamestudio.server.service;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.common.entity.Rating;
import sk.tuke.gamestudio.common.service.RatingService;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    // - zapíše hodnotenie do tabuľky rating. Ak už v tabuľke hodnotenie od daného hráča je, toto sa prepíše. Ak nie, pridá sa nový záznam (riadok).
    @Override
    public void setRating(Rating rating) {
        var ratingInDb = getRatingObject(rating.getGame(), rating.getUsername());
        if (ratingInDb == null) {
            entityManager.persist(rating);
        } else {
            ratingInDb.setRating(rating.getRating());
            ratingInDb.setRatedAt(rating.getRatedAt());
//            entityManager.remove(ratingInDb);
//            entityManager.persist(rating);
        }
    }

    // - vráti priemerné hodnotenie hry, zaokrúhlené na celé číslo  alebo 0 ak sa žiadne hodnotenia danej hry v tabuľke nie sú
    @Override
    public int getAverageRating(String game) {
        var value = entityManager.createNamedQuery("Rating.getAvgRating")
                .setParameter("game", game).getSingleResult();
        return value == null ? 0 : (int) Math.round((Double) value);
    }

    //-  vráti hodnotenie hry s názvom game od hráča s menom username alebo 0 ak sa žiadne také hodnotenie v tabuľke nenájde.
    @Override
    public int getRating(String game, String username) {
        var rating = getRatingObject(game, username);
        return rating == null ? 0 : rating.getRating();
    }

    private Rating getRatingObject(String game, String username) {
        try {
            return entityManager.createNamedQuery("Rating.getRating", Rating.class)
                    .setParameter("game", game)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    // - vymaže údaje v tabuľke rating.
    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
    }
}
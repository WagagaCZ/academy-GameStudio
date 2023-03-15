package sk.tuke.gamestudio.common.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@NamedQuery( name = "Rating.getRating",
        query = "select r from Rating r where r.game = :game and r.username = :username")
@NamedQuery( name = "Rating.getAvgRating",
        query = "select avg(r.rating) from Rating r where r.game = :game")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"game", "username"}))
public class Rating {
    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length = 64)
    private String game;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(columnDefinition = "INT CHECK(rating BETWEEN 1 AND 5)")
    private int rating; // - celé číslo od 1 do 5

    @Column(nullable = false)
    private Date ratedAt;

    public Rating() {
    }

    public Rating(int rating) {
        this.rating = rating;
    }

    public Rating(String game, String username, int rating) {
        this.game = game;
        this.username = username;
        this.rating = rating;
        ratedAt = new Date();
    }

    public long getIdent() {
        return ident;
    }

    public void setIdent(long ident) {
        this.ident = ident;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(Date ratedAt) {
        this.ratedAt = ratedAt;
    }
}
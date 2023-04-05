package sk.tuke.gamestudio.common.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String player;
    private String game;
    private String comment;

    @Column(name = "commentedon")
    private Timestamp commentedOn;

    public Comment(String player, String game, String comment, Timestamp commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public Comment() {}

    @Override
    public String toString() {
        return String.format("%s: %s", player, comment);
    }

    public long getId() { return id; }
    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }
    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Timestamp getCommentedOn() {
        return commentedOn;
    }
    public void setCommentedOn(Timestamp commentedOn) {
        this.commentedOn = commentedOn;
    }


}

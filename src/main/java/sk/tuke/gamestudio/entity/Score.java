package sk.tuke.gamestudio.entity;

import java.util.Date;
import java.util.Formatter;

public class Score {
    private String game;

    private String player;

    private int points;

    private Date playedOn;

    public Score(String game, String player, int points, Date playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        f.format("%10s\t%10s\t%5s\t%4$td.%4$tm.%4$tY %4$tH:%4$tM:%4$tS", game, player, points, playedOn);
        return f.toString();
    }
}

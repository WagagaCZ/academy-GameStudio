package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreException;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String CREATE = """
        CREATE TABLE IF NOT EXISTS score (
            id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
            game VARCHAR(20) NOT NULL,
            player VARCHAR(32) NOT NULL,
            points INT,
            playedOn TIMESTAMP
        )
    """;
    public static final String SELECT = """
        SELECT game, player, points, playedOn 
        FROM score 
        WHERE game = ? 
        ORDER BY points DESC LIMIT 10
    """;
    public static final String DELETE = "DROP TABLE score";
    public static final String INSERT = """
        INSERT INTO score (game, player, points, playedOn) 
        VALUES (?, ?, ?, ?)
    """;

    public ScoreServiceJDBC() {
//        reset();
        createScoreTable();
    }

    public void createScoreTable() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement s = conn.createStatement();
        ) {
            s.executeUpdate(CREATE);
        } catch (SQLException e) {
            throw new ScoreException("Problem creating the Score database table", e);
        }
    }

    @Override
    public void addScore(Score score) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement s = conn.prepareStatement(INSERT)
        ) {
            s.setString(1, score.getGame());
            s.setString(2, score.getPlayer());
            s.setInt(3, score.getPoints());
            s.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            s.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement s = conn.prepareStatement(SELECT);
        ) {
            s.setString(1, game);
            try (ResultSet rs = s.executeQuery()) {
                List<Score> scores = new ArrayList<>();
                while (rs.next()) {
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement s = conn.createStatement();
        ) {
            s.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }
}

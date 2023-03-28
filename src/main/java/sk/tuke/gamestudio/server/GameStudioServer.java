package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio.common.service.RatingService;

import sk.tuke.gamestudio.common.service.ScoreService;
import sk.tuke.gamestudio.server.service.RatingServiceJPA;
import sk.tuke.gamestudio.server.service.ScoreServiceJPA;

@SpringBootApplication
@EntityScan(basePackages = "sk.tuke.gamestudio.common.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class);
        System.out.println("\n*** Server started ***\n");
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public RatingService getRatingService() {
        return new RatingServiceJPA();
    }
}

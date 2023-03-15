package sk.tuke.gamestudio.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.client.game.minesweeper.consoleui.ConsoleUI;
import sk.tuke.gamestudio.client.game.minesweeper.core.Field;
import sk.tuke.gamestudio.client.service.RatingClientServiceREST;
import sk.tuke.gamestudio.client.service.ScoreClientServiceREST;
import sk.tuke.gamestudio.common.service.RatingService;
import sk.tuke.gamestudio.common.service.ScoreService;

@SpringBootApplication
@EntityScan(basePackages = "sk.tuke.gamestudio.common.entity")
public class GameStudioClient {

    //start as a standard app - no web server
    public static void main(String[] args) {
        new SpringApplicationBuilder(GameStudioClient.class)
                .web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runnerSimple() {
        return args -> {
            System.out.println("SpringClient: Hello from SpringBoot.");
        };
    }

    @Bean
    public CommandLineRunner runnerMines(ConsoleUI minesConsole) {
        return args -> {
            minesConsole.newGameStarted(new Field(10, 10, 1));
        };
    }

    @Bean
    public ScoreService getScoreService() {
        return new ScoreClientServiceREST();
    }

    @Bean
    public RatingService getRatingService() {
        return new RatingClientServiceREST();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

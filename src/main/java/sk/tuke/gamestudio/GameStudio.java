package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio.game.minesweeper.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.minesweeper.core.Field;

@SpringBootApplication
public class GameStudio {

    //start as a standard app - no web server
    public static void main(String[] args) {
        new SpringApplicationBuilder(GameStudio.class)
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
            minesConsole.newGameStarted(new Field(10, 10, 10));
        };
    }
}

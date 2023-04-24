package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.connect.core.Field;
import sk.tuke.gamestudio.client.game.connect.core.GameState;
import sk.tuke.gamestudio.client.game.connect.core.Tile;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.util.Date;
import java.util.Formatter;
import java.util.List;

@Controller
@RequestMapping("/connect")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ConnectController extends GameController {

    private Field field = null;
    private GameState state = GameState.PLAYING;
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserController userController;

    // /connect
    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        startOrUpdateGame(row, column);
        return "connect";
    }

    // /connect/new
    @RequestMapping("/new")
    public String newGame() {
        startNewGame();
        return "connect";
    }

    private boolean startOrUpdateGame(Integer row, Integer column) {
        if (field == null) {
            startNewGame();
        }

        boolean justFinished = false;

        if (row != null && column != null) {

            state = field.getState(); //preco mas duplikovany game state, aj tu aj vo field? staci vo field a ak to potrebujes v HTML, tak si tu sprav na to getter

            if (state == GameState.PLAYING) {
                field.makeMove(column);
            }

            state = field.getState();

            if (state != GameState.PLAYING && state != GameState.FULL) {
                justFinished = true;
                if (userController.isLogged()) {
                    scoreService.addScore(new Score( //dlhe riadky treba trochu odenterovat
                            "connect", //tento string mozes dat ako konstantu - refactor -> introduce constant
                            userController.getLoggedUser() + state,
                            field.getScore(),
                            new Date()));
                }
            }
        }

        return justFinished;
    }

    private void startNewGame() {
        field = new Field(9, 9);
        state = field.getState();
    }

    public String getCurrentTime() {
        return new Date().toString();
    }
    
    public Tile[][] getFieldTiles() {
        return field == null ? null : field.getTiles();
    }

    public boolean isPlaying() {
        return field != null && field.getState() == GameState.PLAYING;
    }

    public String getHtmlField() {
        Formatter f = new Formatter();
        f.format("<table class='minefield'> \n");
        for (int r = 0; r < field.getRows(); r++) {
            f.format("<tr>\n");

            for (int c = 0; c < field.getCols(); c++) {
                var tile = field.getTile(r, c);
                f.format("<td style='%s'>\n", getTileStyle(tile));

                if (field.getState() == GameState.PLAYING) {
                    f.format("""
                            <a href='/conntect?r=%d&collumn=%d' style='line-height: 1em;'>
                            <span>%s</span>
                            </a>
                            """, r, c, getTileText(tile));
                } else {
                    f.format("<span>%s</span>", getTileText(tile)); //StringBuilder ani formatter nemaju velmi vyznam, pokial vo vnutri robis konkatenaciu
                }

                f.format("</td>\n");
            }
            f.format("</tr>\n");
        }
        f.format("</table>\n");
        return f.toString();
    }
    
    public String getTileText(Tile tile) {
        return switch(tile.getPlayer()) {
            case 1 -> "1";
            case 2 -> "2";
            case 0 -> "0";
            default -> throw new IllegalArgumentException("Unsupported tile state " + tile.getPlayer());
        };
    }

    public String getTileStyle(Tile tile) {
        return "background-color: " + getPlayerColor(tile.getPlayer()) 
                + "; border-radius: 50%"; //namiesto style si vytvor na toto classy v cssku a tu uz len men classy, komunikuje to ten vyznam lepsie :)
    }

    public String getPlayingStyle() {
        if (field.getState() != GameState.PLAYING) return "";

        return "background-color: " + getPlayerColor(field.getCurrentPlayer())
                + "; border-radius: 10%;";
    }

    private String getPlayerColor(int player) {
        return switch(player) {
            case 1 -> "#ff5555";
            case 2 -> "#5555ff";
            default -> "initial";
        };
    }

    public GameState getState() {
        return state;
    }

    @Override
    protected String getGameName() {
        return "connect";
    }
}

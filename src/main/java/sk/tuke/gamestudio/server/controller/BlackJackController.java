package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.blackjack.core.Card;
import sk.tuke.gamestudio.client.game.blackjack.core.Table;
import sk.tuke.gamestudio.client.game.blackjack.core.Turn;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequestMapping("/blackjack")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlackJackController extends GameController {
    private Table table = new Table();
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserController userController;

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer move) {
        startOrUpdate(move);
        return "blackjack";
    }

    private void startOrUpdate(Integer move) {
        if (table == null) {
            newGame();
        }
        if (move != null) {
            switch (move) {
                case 1 -> {
                    table.drawNewCard();
                }
                case 2 -> {
                    table.switchTurns();
                }
            }
        }
    }

    @RequestMapping("/new")
    private String newGame() {
        if (table.getBank() == 0){
            end();
        }
        table.setup();
        return "blackjack";
    }

    @RequestMapping("/shuffle")
    private String shuffleDeck() {
        table.shuffle();
        return "blackjack";
    }

    @RequestMapping("/end")
    private String end() {
        if (table.getTurn() == Turn.END) {
            if (userController.isLogged()) {
                scoreService.addScore(new Score(getGameName(), userController.getLoggedUser(), table.getBank(), new Date()));
            }
            table = new Table();
        }
        return "blackjack";
    }
    @RequestMapping("/double")
    private String dbl(){
        table.doubleUp();
        return "blackjack";
    }

    public Turn getTurn() {
        return table.getTurn();
    }

    public Card[] getDealersHand() {
        return table.getDealerHand();
    }

    public Card[] getPlayersHand() {
        return table.getPlayerHand();
    }

    public String getCardText(Card card) {
        if(card!= null){
            return card.toString();
        }
        else {
            return null;
        }
    }

    public boolean isPlayersTurn() {
        return table.getTurn() == Turn.PLAYER;
    }

    public boolean isEnd() {
        return table.getTurn() == Turn.END;
    }

    public boolean isPlayerWinner() {
        return table.isPlayerWinner();
    }

    public String getBank() {
        return String.valueOf(table.getBank());
    }

    public String getBet() {
        return String.valueOf(table.getBet());
    }

    public boolean canDouble() {
        return (table.getTurn() == Turn.PLAYER && table.getBank() >= (table.getBet() * 2) && table.getPlayerHand()[2] == null);
    }

    @Override
    protected String getGameName() {
        return "blackjack";
    }
}

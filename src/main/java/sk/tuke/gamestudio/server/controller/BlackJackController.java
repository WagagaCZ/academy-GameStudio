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
import sk.tuke.gamestudio.common.service.ScoreService;

@Controller
@RequestMapping("/blackjack")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlackJackController {
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
        if (move != null){
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
        table.setup();
        return "blackjack";
    }

    @RequestMapping("/shuffle")
    private String shuffleDeck() {
        table.shuffle();
        return "blackjack";
    }

    public Turn getTurn() {
        return table.getTurn();
    }
    public Card[] getDealersHand(){
        return table.getDealerHand();
    }
    public Card[] getPlayersHand(){
        return table.getPlayerHand();
    }
    public String getCardText(Card card){
        return card.toString();
    }
    public boolean isPlayersTurn(){
        return table.getTurn() == Turn.PLAYER;
    }
    public boolean isEnd(){
        return table.getTurn() == Turn.END;
    }
    public boolean isPlayerWinner(){
        return table.isPlayerWinner();
    }
}

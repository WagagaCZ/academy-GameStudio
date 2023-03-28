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
import java.util.List;

@Controller
@RequestMapping("/connect")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ConnectController {

    private Field field = null;
    private GameState state = GameState.PLAYING;
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserController userController;

    // /connect
    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row,@RequestParam(required = false) Integer column){

        startOrUpdateGame(row,column);
        return "connect";
    }


    // /connect/new
    @RequestMapping("/new")
    public String newGame(){
        startNewGame();
        return "connect";
    }



    private boolean startOrUpdateGame(Integer row, Integer column){
        if(field==null){
            startNewGame();
        }

        boolean justFinished=false;

        if(row!=null && column!=null){

            state = field.getState();

            if(state == GameState.PLAYING){
                    field.makeMove(column);
            }

            state = field.getState();

            if(state!=GameState.PLAYING && state!=GameState.FULL){
                justFinished=true;
                if(userController.isLogged()){
                    scoreService.addScore(new Score("connect",userController.getLoggedUser()+state,field.getScore(), new Date()));
                }
            }
        }

        return justFinished;

    }

    private void startNewGame(){
        field = new Field(9,9);
        state = field.getState();
    }

    public String getCurrentTime(){
        return new Date().toString();
    }

    public List<Score> getTopScores(){
        return scoreService.getTopScores("connect");
    }

    public Tile[][] getFieldTiles() {
        if(field==null){
            return null;
        }else{
            return field.getTiles();
        }
    }

    public boolean isPlaying(){
        if(field==null){
            return false;
        }else{
            return (field.getState()== GameState.PLAYING);
        }
    }

    public String getHtmlField(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='minefield'> \n");
        for (int row = 0; row < field.getRows(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getCols(); column++) {
                var tile = field.getTile(row, column);
                sb.append("<td style='" + getTileStyle(tile) + "'>\n");
                if(field.getState()==GameState.PLAYING){
                    sb.append("<a href='/connect?row="+row+"&column="+column+"'>\n");
                    sb.append("<span>" + getTileText(tile) + "</span>");
                    sb.append("</a>\n");
                }else{
                    sb.append("<span>" + getTileText(tile) + "</span>");
                }
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }


    public String getTileText(Tile tile) {
        switch (tile.getPlayer()) {
            case 1:
                return "1";
            case 2:
                return "2";
            case 0:
                return "0";
            default:
                throw new IllegalArgumentException("Unsupported tile state " + tile.getPlayer());
        }
    }

    public String getTileStyle(Tile tile) {
        if (tile.getPlayer() == 1) {
            return "background-color: #ff5555;border-radius: 50%;";
        } else if (tile.getPlayer() == 2) {
            return "background-color: #5555ff;border-radius: 50%;";
        } else {
            return "border-radius: 50%;";
        }
    }

    public String getPlayingStyle() {
        if (field.getState() != GameState.PLAYING) return "";
        if (field.getCurrentPlayer() == 1) {
            return "background-color: #ff5555;border-radius: 10%;";
        } else if (field.getCurrentPlayer() == 2) {
            return "background-color: #5555ff;border-radius: 10%;";
        } else {
            return "";
        }
    }

    public GameState getState() {
        return state;
    }
}

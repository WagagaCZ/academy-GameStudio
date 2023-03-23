package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.client.game.minesweeper.core.Clue;
import sk.tuke.gamestudio.client.game.minesweeper.core.Field;
import sk.tuke.gamestudio.client.game.minesweeper.core.GameState;
import sk.tuke.gamestudio.client.game.minesweeper.core.Tile;
import sk.tuke.gamestudio.common.entity.Score;
import sk.tuke.gamestudio.common.service.ScoreService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/mines")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesController {

    private Field field = null;
    private boolean marking = false;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserController userController;

    // /mines
    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row,@RequestParam(required = false) Integer column){

        startOrUpdateGame(row,column);
        return "mines";
    }

    // /mines/chmode
    @RequestMapping("/chmode")
    public String changeMode(){
        changeGameMode();
        return "mines";
    }

    // /mines/new
    @RequestMapping("/new")
    public String newGame(){
        startNewGame();
        return "mines";
    }


    private void startOrUpdateGame(Integer row, Integer column){
        if(field==null){
            startNewGame();
        }

        if(row!=null && column!=null){

            GameState stateBeforeMove = field.getState();

            if(stateBeforeMove == GameState.PLAYING){
                if(marking){
                    field.markTile(row,column);
                }else {
                    field.openTile(row, column);
                }
            }

            if(field.getState()==GameState.SOLVED && stateBeforeMove!=field.getState()){
                if(userController.isLogged()){
                    scoreService.addScore(new Score("mines",userController.getLoggedUser(),field.getScore(), new Date()));
                }
            }
        }

    }

    private void startNewGame(){
        field = new Field(9,9,2);
        marking = false;
    }

    private void changeGameMode(){
        if(field==null){
            startNewGame();
        }
        marking=!marking;
    }

    public  boolean isMarking(){
        return marking;
    }

    public String getCurrentTime(){
        return new Date().toString();
    }

    public List<Score> getTopScores(){
        return scoreService.getTopScores("mines");
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
            return (field.getState()==GameState.PLAYING);
        }
    }

    public String getHtmlField(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='minefield'> \n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                var tile = field.getTile(row, column);
                sb.append("<td class='" + getTileClass(tile) + "'>\n");
                if(field.getState()==GameState.PLAYING){
                    sb.append("<a href='/mines?row="+row+"&column="+column+"'>\n");
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
        switch (tile.getState()) {
            case CLOSED:
                return "-";
            case MARKED:
                return "M";
            case OPEN:
                if (tile instanceof Clue) {
                    return String.valueOf(((Clue) tile).getValue());
                } else {
                    return "X";
                }
            default:
                throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
        }
    }

    public String getTileClass(Tile tile) {
        switch (tile.getState()) {
            case OPEN:
                if (tile instanceof Clue)
                    return "open" + ((Clue) tile).getValue();
                else
                    return "mine";
            case CLOSED:
                return "closed";
            case MARKED:
                return "marked";
            default:
                throw new RuntimeException("Unexpected tile state");
        }
    }






}

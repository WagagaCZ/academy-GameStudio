package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.tuke.gamestudio.client.game.minesweeper.core.Clue;
import sk.tuke.gamestudio.client.game.minesweeper.core.Field;
import sk.tuke.gamestudio.client.game.minesweeper.core.Tile;

import java.util.Date;

@Controller
@RequestMapping("/mines")
public class MinesController {

    private Field field = new Field(9,9,10);

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row,@RequestParam(required = false) Integer column){
        System.out.println("row= " + row);
        System.out.println("column= " + column);

        if(row!=null && column!=null){
            field.openTile(row,column);
        }

        return "mines";
    }


    public String getCurrentTime(){
        return new Date().toString();
    }

    public String getHtmlField(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='minefield'> \n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                var tile = field.getTile(row, column);
                sb.append("<td class='" + getTileClass(tile) + "'>\n");
                sb.append("<a href='/mines?row="+row+"&column="+column+"'>\n");
                sb.append("<span>" + getTileText(tile) + "</span>");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }


    private String getTileText(Tile tile) {
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

    private String getTileClass(Tile tile) {
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

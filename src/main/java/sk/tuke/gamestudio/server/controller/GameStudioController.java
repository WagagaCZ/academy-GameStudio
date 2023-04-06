package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameStudioController {

    @RequestMapping("/")
    public String mainPage(){return "index";}

    @RequestMapping("/minesweeperJS")
    public String minesweeperJS(){return "minesweeperJS";}

    @RequestMapping("/game1024")
    public String game1024(){return "game1024";}

    @RequestMapping("/candrycrush")
    public String candycr(){return "candrycrush";}

    @RequestMapping("/test")
    public String testpage(){return "testpage";}

}
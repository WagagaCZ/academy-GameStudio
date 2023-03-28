package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameStudioController {

    @RequestMapping("/")
    public String mainPage(){return "index";}

    @RequestMapping("/minesweeperJS")
    public String minesweeperJS(){return "minesweeperJS";}

    @RequestMapping("/test")
    public String testpage(){return "testpage";}

}
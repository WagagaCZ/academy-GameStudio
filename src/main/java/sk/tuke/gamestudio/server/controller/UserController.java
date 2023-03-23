package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {


    private String loggedUser = null;

    private final String PASS_PHRASE = "heslo";

    @RequestMapping("/login")
    public String login (String login, String password) {
        if (PASS_PHRASE.equals(password)) {
            loggedUser = login.trim();
            if (loggedUser.length() > 0) {
                return "redirect:/";
            }
        }
        return logout();
    }

    @RequestMapping("/logout")
    public String logout(){
        loggedUser = null;
        return "redirect:/";
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged(){
        return loggedUser!=null;
    }


}

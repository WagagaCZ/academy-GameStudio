package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {


    private String loggedUser = null;

    private final String PASS_PHRASE = "heslo";

    @RequestMapping("/login")
    public String login (String login, String password, Model model) {
        if (PASS_PHRASE.equals(password)) {
            loggedUser = login.trim();
            if (loggedUser.length() > 0) {
                //prepareModel(model);
                return "redirect:/";
            }
        }
        return logout(model);
    }

    @RequestMapping("/logout")
    public String logout(Model model){
        loggedUser = null;
        //prepareModel(model);
        return "redirect:/";
    }

    @GetMapping("loginPage") 
    public String loginPage() {
 
        return "login";
    }

    private void prepareModel(Model model){
        if(loggedUser==null) {
            model.addAttribute("username", "");
        }else {
            model.addAttribute("username", loggedUser);
        }
        model.addAttribute("logged",isLogged());
        //System.out.println("prepareModel");
    }

    @RequestMapping("/api/v2/user")
    @ResponseBody
    public Map<String, Object> getUserInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("loggedUser", getLoggedUser());
        return result;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged(){
        return loggedUser!=null;
    }


}

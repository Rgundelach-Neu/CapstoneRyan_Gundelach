/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 1:29 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers;
 */
package Rgundelach.Capstone.PageControllers.LogIn;

import Rgundelach.Capstone.Models.UserManager;
import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;
    @GetMapping("/loginUser")
    public String LoginForm(Model model) {
        model.addAttribute("LoginInformation", new LoginInformation());
        return "login";
    }
    @PostMapping("/loginUser")
    public String Loginpost(@ModelAttribute LoginInformation loginInformation, Model model) {
        model.addAttribute("LoginInformation", loginInformation);
        //Check with mongo
        /*Users possibleUser = userManager.findUser(new Users(loginInformation.getUserName(),null, loginInformation.getUserPassword()));
        if(possibleUser != null){
            GlobalObjects.SignInUser(possibleUser);
            //Create user in inmemorymanager
        }

         */
        Users users = new Users(loginInformation.getUserName(), null, loginInformation.getUserPassword());
        inMemoryUserDetailsManager.createUser(
                        User.withUsername(users.getName())
                        .password(users.getPassword())
                        .roles("USER").build());

        return "HomeServerPage";
    }
}

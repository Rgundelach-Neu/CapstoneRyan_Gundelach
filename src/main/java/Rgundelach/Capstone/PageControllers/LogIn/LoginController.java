/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 1:29 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers;
 */
package Rgundelach.Capstone.PageControllers.LogIn;

import Rgundelach.Capstone.Models.Global.GlobalObjects;
import Rgundelach.Capstone.Models.UserManager;
import Rgundelach.Capstone.Models.Users;
import Rgundelach.Capstone.PasswordManager.SaltedHash;
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
    UserManager userManager;
    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;
    @GetMapping("/loginUser")
    public String LoginForm(Model model) {
        try {
            if (inMemoryUserDetailsManager.userExists(GlobalObjects.getCurrentUser().getName())) {
                return "redirect:/Home";
            }
        }catch (NullPointerException exception){

        }
        model.addAttribute("LoginInformation", new LoginInformation());
        return "login";
    }
    @PostMapping("/loginUser")
    public String Loginpost(@ModelAttribute LoginInformation loginInformation, Model model) {
        model.addAttribute("LoginInformation", loginInformation);
        //Check with mongo
        Users possibleUser = userManager.getUser(loginInformation.getUserName());

        if(possibleUser != null){
            if(new SaltedHash().passwordEncoder().matches(loginInformation.getUserPassword(), possibleUser.getPassword())){
                inMemoryUserDetailsManager.createUser(
                        User.withUsername(possibleUser.getName())
                                .password(possibleUser.getPassword())
                                .roles(possibleUser.getRoles()).build());
                GlobalObjects.SetCurrentUser(possibleUser);
                return "redirect:/Home";
            }

        }
    return "login";
    }
}

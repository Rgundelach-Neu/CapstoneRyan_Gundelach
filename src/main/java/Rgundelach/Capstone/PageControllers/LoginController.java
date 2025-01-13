/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 1:29 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers;
 */
package Rgundelach.Capstone.PageControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/loginUser")
    public String LoginForm(Model model) {
        model.addAttribute("LoginInformation", new LoginInformation());
        return "login";
    }
    @PostMapping("/loginUser")
    public String Loginpost(@ModelAttribute LoginInformation loginInformation, Model model) {
        model.addAttribute("LoginInformation", loginInformation);

        //Spring security will handle it here out
        //Remove user after logout or update of core profile settings
        return "HomeServerPage";
    }
}

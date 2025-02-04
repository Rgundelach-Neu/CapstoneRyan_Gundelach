/**
 * @author rgundelach
 * @createdOn 2/2/2025 at 11:13 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.LogOut;
 */
package Rgundelach.Capstone.PageControllers.LogOut;

import Rgundelach.Capstone.Models.Global.GlobalObjects;
import Rgundelach.Capstone.Models.Users;
import Rgundelach.Capstone.PageControllers.LogIn.LoginController;
import Rgundelach.Capstone.PageControllers.LogIn.LoginInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogOutController {

    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @GetMapping("/Logout")
    public String Logout(Model model){
        inMemoryUserDetailsManager.deleteUser(GlobalObjects.getCurrentUser().getName());
        GlobalObjects.SetCurrentUser(new Users());
        model.addAttribute("LoginInformation", new LoginInformation());
        return "login";
    }
}

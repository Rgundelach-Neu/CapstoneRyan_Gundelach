/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:11 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Profile;
 */
package Rgundelach.Capstone.PageControllers.Profile;

import Rgundelach.Capstone.Models.Global.GlobalObjects;
import Rgundelach.Capstone.Models.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ProfileController {

    @Autowired
    UserManager userManager;


    @GetMapping("/Home/Profile")
    public String getProfile(Model model){
        model.addAttribute("UserInfo", GlobalObjects.getCurrentUser());

        return "profile";
    }
    @PutMapping("/Home/Profile/Update")
    public String updateUserProfile(Model model){
        model.addAttribute("UserInfo", GlobalObjects.getCurrentUser());

        return "profile";
    }
}

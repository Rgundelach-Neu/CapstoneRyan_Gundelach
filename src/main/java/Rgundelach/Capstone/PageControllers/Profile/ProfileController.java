/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:11 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Profile;
 */
package Rgundelach.Capstone.PageControllers.Profile;

import Rgundelach.Capstone.Models.Global.GlobalObjects;
import Rgundelach.Capstone.Models.UserManager;
import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ProfileController {

    @Autowired
    UserManager userManager;


    @GetMapping("/Home/Profile")
    @PreAuthorize("hasRole('USER')")
    public String getProfile(Model model){
        if(GlobalObjects.isAllowed()) {
            ProfileInformation profileInformation = new ProfileInformation();
            profileInformation.setUsername(GlobalObjects.getCurrentUser().getName());
            profileInformation.setEmail(GlobalObjects.getCurrentUser().getEmail());
            model.addAttribute("UserInfo", profileInformation);
            return "profile";
        }
        else{
            return "redirect:/loginUser";
        }
    }
    @PostMapping("/Home/Profile/Update")
    @PreAuthorize("hasRole('USER')")
    public String updateUserProfile(@ModelAttribute ProfileInformation UpdatedUser){
        Users user = new Users(UpdatedUser.getUsername(), UpdatedUser.getEmail(), UpdatedUser.getPassword());
        userManager.updateUser(user,GlobalObjects.getCurrentUser().getName());

        return "profile";
    }
}

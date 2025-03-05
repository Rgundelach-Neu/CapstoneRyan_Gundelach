/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:03 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.HomePage;
 */
package Rgundelach.Capstone.PageControllers.HomePage;


import Rgundelach.Capstone.Models.Global.GlobalObjects;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/Home")
    @PreAuthorize("hasRole('USER')")
    public String HomeGet(){
        if(GlobalObjects.isAllowed()) {
            return "HomeServerPage";
        }else {
            return "redirect:/loginUser";
        }
    }
}

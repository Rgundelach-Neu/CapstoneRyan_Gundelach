/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:03 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.HomePage;
 */
package Rgundelach.Capstone.PageControllers.HomePage;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/Home")
    public String HomeGet(){


        return "HomeServerPage";
    }
}

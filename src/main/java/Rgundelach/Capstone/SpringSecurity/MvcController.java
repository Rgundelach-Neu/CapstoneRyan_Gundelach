/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 12:42 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.SpringSecurity;
 */
package Rgundelach.Capstone.SpringSecurity;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class MvcController {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/loginUser").setViewName("login");
        registry.addViewController("/CreateUser").setViewName("CreateUser");
        registry.addViewController("/Home").setViewName("HomeServerPage");
        registry.addViewController("/Home/Server").setViewName("Server");
        registry.addViewController("/Home/Profile").setViewName("profile");
        registry.addViewController("/Home/Profile/Update").setViewName("profile");
        registry.addViewController("/Logout");
    }
}

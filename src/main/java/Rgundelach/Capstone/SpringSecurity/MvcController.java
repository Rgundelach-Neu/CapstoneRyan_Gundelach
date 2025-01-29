/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 12:42 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.SpringSecurity;
 */
package Rgundelach.Capstone.SpringSecurity;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class MvcController {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/greeting").setViewName("greeting");
        registry.addViewController("/loginUser").setViewName("login");
        registry.addViewController("/CreateUser").setViewName("CreateUser");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/Styles/**")) {
            registry.addResourceHandler("/Styles/**").addResourceLocations("src/main/resources/static/Styles/");
        }
    }
}

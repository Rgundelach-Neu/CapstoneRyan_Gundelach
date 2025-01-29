/**
    @author rgundelach
    @createdOn 1/13/2025 at 9:42 AM
    @projectName Capstone
 @packageName Rgundelach.Capstone.PageControllers.CreateAccount;*/

package Rgundelach.Capstone.PageControllers.CreateAccount;

import Rgundelach.Capstone.Models.UserManager;
import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@EnableMongoRepositories
public class CreateUserController {

    @Autowired
    UserManager userManager;

    @GetMapping("/CreateUser")
    public String GetCreateUser(Model model){
        model.addAttribute("CreateUserInformation", new CreateUserInformation());
        return "CreateUser";
    }


    @PostMapping("/CreateUser")
    public String PostCreateUser(Model model,@ModelAttribute CreateUserInformation createUserInformation){
        Users user = new Users(createUserInformation.getUsername(),createUserInformation.getEmail(),createUserInformation.getPassword());
        System.out.println("NextLine?");
        if(userManager.getUser(createUserInformation.getUsername()).getClass() == Users.class){
         //ErrorPage
        }else {
            userManager.CreateUser(user);
        }
        //create mongo user
        //Sign them in
        return "HomeServerPage";
    }

}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
        List<String> ErrorMessages = CheckUserInput(createUserInformation);
        if(!ErrorMessages.isEmpty()){
            model.addAttribute("Error", ErrorMessages);
            model.addAttribute("CreateUserInformation", createUserInformation);
            return "CreateUser";
        }
        Users user = new Users(createUserInformation.getUsername(),createUserInformation.getEmail(),createUserInformation.getPassword());
       // System.out.println("NextLine?");
        try {
            if (userManager.getUser(createUserInformation.getUsername()).getClass() == Users.class) {
                ErrorMessages.add("Email Already in use!");
                model.addAttribute("Error", ErrorMessages );
                model.addAttribute("CreateUserInformation", createUserInformation);
                return "CreateUser";
            }
        }catch (NullPointerException exception){
            userManager.CreateUser(user);
        }
        //create mongo user
        //Sign them in
        return "redirect:/Home";
    }

    private List<String> CheckUserInput(CreateUserInformation info){

        List<String> returnValues = new ArrayList<>();
        if(info.getEmail().isEmpty()){ returnValues.add("Email Cannot Be Empty!");}
        if(info.getUsername().isEmpty()){returnValues.add("UserName Cannot Be Empty!");}
        if(info.getPassword().isEmpty()){returnValues.add("UserName Cannot Be Empty!");}
        if(!returnValues.isEmpty()){
            return returnValues;
        }
        if(!info.getEmail().contains("@")){
            returnValues.add("Email Must Be a Proper Email!");
        }
        boolean HasCapital = false;
        boolean HasLowerCase =false;
        boolean HasThreeNumbers = false;
        int NumberCount = 0;
        boolean HasSpecialCharacter = false;
        if(info.getUsername().length() > 15){
            returnValues.add("Your username can only be 14 characters long!");
        }
        if(info.getPassword().length() >= 16){
            returnValues.add("Your password must be under 16 characters");
        }
        for(int i =0; i < info.getPassword().length(); i++){

            if(!HasCapital && Character.isUpperCase(info.getPassword().charAt(i))){
                HasCapital = true;
            }
            if(!HasLowerCase && Character.isLowerCase(info.getPassword().charAt(i))){
                HasLowerCase = true;
            }
            if(!HasThreeNumbers && Character.isDigit(info.getPassword().charAt(i))){
                if(NumberCount != 3){
                    NumberCount++;
                }else{
                    HasThreeNumbers = true;
                }
            }
            if(!HasSpecialCharacter && isSpecialCharacter(info.getPassword().charAt(i))){
                HasSpecialCharacter = true;
            }
        }
        if(!HasCapital){
            returnValues.add("Your Password Needs a Capital Letter!");
        }
        if(!HasLowerCase){
            returnValues.add("Your Password Needs a LowerCase Letter!");
        }
        if(!HasThreeNumbers){
            returnValues.add("Your Password Needs to Have 3 Numbers!");
        }
        if(!HasSpecialCharacter){
            returnValues.add("Your Password Needs a Special Character!");
        }
        return returnValues;
    }

    private boolean isSpecialCharacter(char c){
        if(!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) return true;
        else return false;
    }
}

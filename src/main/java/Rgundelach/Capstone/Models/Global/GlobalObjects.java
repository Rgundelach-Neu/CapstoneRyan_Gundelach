/**
 * @author rgundelach
 * @createdOn 1/15/2025 at 11:27 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Global;
 */
package Rgundelach.Capstone.Models.Global;

import Rgundelach.Capstone.Models.UserManager;
import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

public class GlobalObjects {
    static Users currentUser = null;

    public static void SetCurrentUser(Users user){
        currentUser = user;
    }
    public static Users getCurrentUser(){
        return  currentUser;
    }
}

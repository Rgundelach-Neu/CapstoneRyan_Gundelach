/**
 * @author rgundelach
 * @createdOn 1/15/2025 at 10:38 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Models;
 */
package Rgundelach.Capstone.Models;

import Rgundelach.Capstone.MongoDB.DAL.MongoDAL;
import Rgundelach.Capstone.MongoDB.UserRepository;
import Rgundelach.Capstone.PasswordManager.SaltedHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.List;

@Component
public class UserManager {
    @Autowired
    public UserRepository userRepository;
    //MongoDAL DAL = new MongoDAL;

    //private List<Users> users = DAL.findAll();

    //TODO: Do error handling
    public Users findUser(Users users){
        if(UserExists(users)){
           return getUser(users.getName());
        }
        return null;
    }

    public void CreateUser(Users user){
        addUser(user);
    }
    public void addUser(Users users){
        try {
            userRepository.save(users);
        }catch(Exception e){
            System.out.println("Error" + e);
        }
    }
    public boolean UserExists(Users users){
        try {

        List<Users> allUsers = userRepository.findAll();
        for (Users u : allUsers) {
            if (u.getName().equals(users.getName())) {
                return true;
            }

        }
        }catch (Exception e){
            System.out.println("Error "+e);
            return false;
        }
        return false;
    }
    public Users getUser(String userName){
        List<Users> allUsers = userRepository.findAll();
        for (Users u : allUsers) {
            if(u.getName().equals(userName)){
                return u;
            }
        }
        return null;
    }

    public List<Users> findAll(){
        return userRepository.findAll();

    }
    public boolean updateUser(Users users,String initialUsername){
        try {
            Users updatedUser = getUser(initialUsername);
            updatedUser = users;
            userRepository.save(updatedUser);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
    public boolean deleteUser(Users users){
        try {
            userRepository.delete(users);
            return true;
        }catch (Exception e){
            return  false;
        }

    }

}

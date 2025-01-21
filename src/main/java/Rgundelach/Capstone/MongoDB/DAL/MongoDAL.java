/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 10:10 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.MongoDB;
 */
package Rgundelach.Capstone.MongoDB.DAL;

import Rgundelach.Capstone.Models.Users;
import Rgundelach.Capstone.MongoDB.DAL.IMongoDAL;
import Rgundelach.Capstone.MongoDB.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDAL implements IMongoDAL {

    @Autowired
    private UserRepository userRepository;

    public void addUser(Users users){
        userRepository.save(users);
    }
    public boolean findUser(Users users){
        List<Users> allUsers = userRepository.findAll();
        for (Users u : allUsers) {
         if(u.getEmail().equals(users.getEmail())){
             return true;
         }
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

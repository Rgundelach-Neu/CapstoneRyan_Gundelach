/**
 * @author rgundelach
 * @createdOn 1/15/2025 at 10:38 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Models;
 */
package Rgundelach.Capstone.Models;

import Rgundelach.Capstone.MongoDB.DAL.MongoDAL;
import org.springframework.stereotype.Component;

@Component
public class UserManager {
    MongoDAL DAL = new MongoDAL();//Should use a generic DAL Inheritance class

    //private List<Users> users = DAL.findAll();

    //TODO: Do error handling
    public Users findUser(Users users){
        if(DAL.findUser(users)){
           return DAL.getUser(users.getName());
        }
        return null;
    }

    public void CreateUser(Users user){
        DAL.addUser(user);
    }


}

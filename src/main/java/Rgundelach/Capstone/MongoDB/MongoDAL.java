/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 10:10 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.MongoDB;
 */
package Rgundelach.Capstone.MongoDB;

import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MongoDAL {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addUser")
    public void addStudent(@RequestBody Users users){
        userRepository.save(users);
    }
}

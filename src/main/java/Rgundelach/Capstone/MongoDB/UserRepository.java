/**
 * @author rgundelach
 * @createdOn 10/4/2024 at 1:24 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.MongoDB;
 */
package Rgundelach.Capstone.MongoDB;


import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<Users,String> {

}

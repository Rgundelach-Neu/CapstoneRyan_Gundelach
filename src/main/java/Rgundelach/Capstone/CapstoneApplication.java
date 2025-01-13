package Rgundelach.Capstone;

import Rgundelach.Capstone.Models.Users;
import Rgundelach.Capstone.MongoDB.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories
public class CapstoneApplication {
	@Autowired
	UserRepository userRepository;

	List<Users> usersList = new ArrayList<Users>();
	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}

}

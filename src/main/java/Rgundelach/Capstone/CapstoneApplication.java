package Rgundelach.Capstone;

import Rgundelach.Capstone.Docker.Kubernetes;
import Rgundelach.Capstone.Models.Global.GlobalObjects;
import Rgundelach.Capstone.MongoDB.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = { "Rgundelach.Capstone" })
@EnableMongoRepositories
public class CapstoneApplication implements CommandLineRunner {


	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

	}
	/*@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
}*/
}

/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 12:56 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.SpringSecurity;
 */
package Rgundelach.Capstone.SpringSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        List<UserDetails> userDetails = new ArrayList<>();
        userDetails.add(User.withUsername("ryan").password(passwordEncoder().encode("ryan")).roles("ADMIN").build());
        userDetails.add(User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build());
        return new InMemoryUserDetailsManager(userDetails);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/","/loginUser").permitAll().anyRequest().authenticated()); // Remove unnecessary items later


        return http.build();
    }
}

/**
 * @author rgundelach
 * @createdOn 1/8/2025 at 12:56 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.SpringSecurity;
 */
package Rgundelach.Capstone.SpringSecurity;

import Rgundelach.Capstone.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('USER')")
public class SpringSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests

                .requestMatchers(HttpMethod.GET,"/loginUser").permitAll()
                .requestMatchers(HttpMethod.GET,"/CreateUser").permitAll()
                .requestMatchers(HttpMethod.POST,"/loginUser").permitAll()
                .requestMatchers(HttpMethod.POST,"/CreateUser").permitAll()
                .requestMatchers(HttpMethod.GET,"/Home").permitAll()
                .requestMatchers(HttpMethod.POST,"/Home").permitAll()
                .requestMatchers(HttpMethod.GET,"/Home/Server").permitAll()
                .requestMatchers(HttpMethod.POST,"/Home/Server").permitAll()
                .requestMatchers(HttpMethod.GET,"/Home/Profile").permitAll()
                .requestMatchers(HttpMethod.POST,"/Home/Profile").permitAll()
                .requestMatchers("Styles/**").permitAll()
                ); // Remove unnecessary items late


        return http.build();
    }
    @Bean
    public static InMemoryUserDetailsManager DefaultMemoryManager(){
        List<UserDetails> userDetails = new ArrayList<>();
        return new InMemoryUserDetailsManager(userDetails);

    }
}

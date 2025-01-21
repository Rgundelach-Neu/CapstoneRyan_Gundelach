/**
 * @author rgundelach
 * @createdOn 10/4/2024 at 12:59 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Models;
 */
package Rgundelach.Capstone.Models;

import Rgundelach.Capstone.PasswordManager.SaltedHash;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("UserAccounts")
public class Users {

    @Id
    private String name;

    private List<String> roles = new ArrayList<>();
    private String Email;
    private String password;

    public Users( String name, String email, String password) {
        this.name = name;
        Email = email;
        this.password = new SaltedHash().passwordEncoder().encode(password);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoles() {

        return roles.get(0);
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

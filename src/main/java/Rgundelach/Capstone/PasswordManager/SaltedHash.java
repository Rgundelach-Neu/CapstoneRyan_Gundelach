/**
 * @author rgundelach
 * @createdOn 10/2/2024 at 12:51 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PasswordManager;
 */
package Rgundelach.Capstone.PasswordManager;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.net.PasswordAuthentication;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class SaltedHash {


    /*
     *   Return: the hashed password
     *   Variable: password =>  the password
     *   Definition: Private function to encrypt the password so users are unable to handle their own salt and hash
     */
    private String SaltAndHashPassword(String password){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(),salt,43903,64);
        byte[] HashedPassword = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            HashedPassword = factory.generateSecret(spec).getEncoded();
        }catch (NoSuchAlgorithmException exception){
            System.out.println("Algorithm Not Found (PasswordManager SaltAndHashPasssword Error)");
        }catch (InvalidKeySpecException SpecException) {
            System.out.println("Algorithm Not Found (PasswordManager SaltAndHashPasssword Error)");
        }
        if(HashedPassword == null){
            throw new IllegalArgumentException();
        }
        return HashedPassword.toString();
    };
    private String PasswordBcrypt(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String enc = encoder.encode(password);
        if(encoder.matches(password,enc)){
            return enc;
        }
        return null;
    }
    /*
    *   Return: the password that is encrypted to be placed into the database
    *   Variable: password => the un encrypted password
    *   Definition: Takes in the password and encrypts the password and hides it from the user
    */
    public String PasswordEncryption(String password){
        return PasswordBcrypt(password);
    }


}

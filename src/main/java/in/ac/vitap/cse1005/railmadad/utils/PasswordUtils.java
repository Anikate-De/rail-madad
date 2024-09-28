package in.ac.vitap.cse1005.railmadad.utils;

import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public void checkPasswordStrength(String password) {
    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
      throw new WeakPasswordException();
    }
  }

  public String hashPassword(String password) {
    return passwordEncoder.encode(password);
  }
}

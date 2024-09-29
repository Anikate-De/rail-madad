package in.ac.vitap.cse1005.railmadad.utils;

import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class PasswordUtils {

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public static void checkPasswordStrength(String password) {
    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=~])(?=\\S+$).{8,}$")) {
      throw new WeakPasswordException();
    }
  }

  public static String hashPassword(String password) {
    return passwordEncoder.encode(password);
  }

  public static boolean matchPassword(String password, String passwordHash) {
    return passwordEncoder.matches(password, passwordHash);
  }
}

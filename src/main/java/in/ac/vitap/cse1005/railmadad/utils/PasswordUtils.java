package in.ac.vitap.cse1005.railmadad.utils;

import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Utility class for handling password operations. */
@UtilityClass
public class PasswordUtils {

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /**
   * Checks the strength of the provided password.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * PasswordUtils.checkPasswordStrength("StrongP@ssw0rd");
   * }</pre>
   *
   * @param password the password to be checked
   * @throws WeakPasswordException if the password does not meet the strength criteria
   */
  public static void checkPasswordStrength(String password) {
    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=~])(?=\\S+$).{8,}$")) {
      throw new WeakPasswordException();
    }
  }

  /**
   * Hashes the provided password using BCrypt.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * String hashedPassword = PasswordUtils.hashPassword("StrongP@ssw0rd");
   * }</pre>
   *
   * @param password the password to be hashed
   * @return the hashed password
   */
  public static String hashPassword(String password) {
    return passwordEncoder.encode(password);
  }

  /**
   * Matches the provided password with the hashed password.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * boolean isMatch = PasswordUtils.matchPassword("StrongP@ssw0rd", hashedPassword);
   * }</pre>
   *
   * @param password the plain text password
   * @param passwordHash the hashed password
   * @return true if the password matches the hash, false otherwise
   */
  public static boolean matchPassword(String password, String passwordHash) {
    return passwordEncoder.matches(password, passwordHash);
  }
}

package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.generateTokenFromUserClaims;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.checkPasswordStrength;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.hashPassword;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.matchPassword;

import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/** Service class for handling officer-related operations. */
@Service
public class OfficerService {
  private final OfficerRepository officerRepository;

  /**
   * Constructs an OfficerService with the specified repository.
   *
   * @param officerRepository the repository for officer data
   */
  @Autowired
  public OfficerService(OfficerRepository officerRepository) {
    this.officerRepository = officerRepository;
  }

  /**
   * Signs up a new officer with the provided details.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Officer officer = new Officer();
   * officerService.signup(officer, "password");
   * }</pre>
   *
   * @param officer the officer to be signed up
   * @param password the password for the officer
   * @return the saved officer
   * @throws IncompleteDetailsException if any of the required details are missing
   * @throws WeakPasswordException if the password is weak
   */
  public Officer signup(@NonNull Officer officer, String password) {
    if (officer.getFirstName() == null || password == null) {
      throw new IncompleteDetailsException();
    }

    checkPasswordStrength(password);
    officer.setPasswordHash(hashPassword(password));

    officer = officerRepository.save(officer);
    return officer;
  }

  /**
   * Logs in an officer with the provided ID and password.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * String token = officerService.login(12345L, "password");
   * }</pre>
   *
   * @param id the ID of the officer
   * @param password the password of the officer
   * @return a JWT token for the authenticated officer
   * @throws IncompleteDetailsException if any of the required details are missing
   * @throws EntityNotFoundException if the officer is not found
   * @throws PasswordMismatchException if the password does not match
   */
  public String login(long id, String password) {
    if (id == 0 || password == null) {
      throw new IncompleteDetailsException();
    }

    Optional<Officer> officer = officerRepository.findById(id);

    if (officer.isEmpty()) {
      throw new EntityNotFoundException();
    }

    if (!matchPassword(password, officer.get().getPasswordHash())) {
      throw new PasswordMismatchException();
    }

    officerRepository.updateLastLoginById(
        Instant.ofEpochMilli(System.currentTimeMillis()), officer.get().getId());

    return generateTokenFromUserClaims(
        UserClaims.builder()
            .id(String.valueOf(officer.get().getId()))
            .role(UserRole.OFFICER)
            .user(officer.get())
            .build(),
        60 * 60 * 1000);
  }
}

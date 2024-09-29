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
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficerService {
  private final OfficerRepository officerRepository;

  @Autowired
  public OfficerService(OfficerRepository officerRepository) {
    this.officerRepository = officerRepository;
  }

  public Officer signup(Officer officer, String password) {
    if (officer.getFirstName() == null || password == null) {
      throw new IncompleteDetailsException();
    }

    checkPasswordStrength(password);
    officer.setPasswordHash(hashPassword(password));

    officer = officerRepository.save(officer);
    return officer;
  }

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
            .build(),
        60 * 60 * 1000);
  }
}

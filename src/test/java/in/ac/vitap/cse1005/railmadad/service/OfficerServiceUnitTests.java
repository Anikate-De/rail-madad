package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.hashPassword;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OfficerServiceUnitTests {

  @Mock private OfficerRepository officerRepository;
  @InjectMocks private OfficerService officerService;

  @Test
  public void testSignUp_WithIncompleteDetails() {
    Officer officer = new Officer();
    String password = "password";

    assertThrows(IncompleteDetailsException.class, () -> officerService.signup(officer, password));
  }

  @Test
  public void testSignUp_WithNoPassword() {
    Officer officer = Officer.builder().firstName("John").build();

    assertThrows(IncompleteDetailsException.class, () -> officerService.signup(officer, null));
  }

  @Test
  public void testSignUp_WithWeakPassword() {
    Officer officer = Officer.builder().firstName("John").build();
    String password = "password";

    assertThrows(WeakPasswordException.class, () -> officerService.signup(officer, password));
  }

  @Test
  public void testSignUp_Successful() {
    Officer officer = Officer.builder().firstName("John").build();
    String password = "strongPassword123!";

    List<Officer> officers = new ArrayList<>();
    Mockito.when(officerRepository.save(Mockito.any(Officer.class)))
        .thenAnswer(
            invocation -> {
              Officer savedOfficer = invocation.getArgument(0);
              officers.add(savedOfficer);
              return savedOfficer;
            });
    Mockito.when(officerRepository.findAll()).thenReturn(officers);

    officerService.signup(officer, password);

    List<Officer> fetchedOfficers = (List<Officer>) officerRepository.findAll();
    assertEquals(1, fetchedOfficers.size());
    assertEquals("John", fetchedOfficers.getFirst().getFirstName());
  }

  @Test
  public void testLogin_WithIncompleteDetails() {
    long id = 1L;
    String password = "password";

    assertThrows(IncompleteDetailsException.class, () -> officerService.login(0L, password));
    assertThrows(IncompleteDetailsException.class, () -> officerService.login(id, null));
  }

  @Test
  public void testLogin_WithMissingID() {
    long id = 1L;
    String password = "password";

    Mockito.when(officerRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> officerService.login(id, password));
  }

  @Test
  public void testLogin_WithMismatchedPassword() {
    long id = 1L;
    String password = "password";

    Officer officer = Officer.builder().firstName("John").id(id).build();
    officer.setPasswordHash("hashedPassword");

    Mockito.when(officerRepository.findById(id)).thenReturn(Optional.of(officer));

    assertThrows(PasswordMismatchException.class, () -> officerService.login(id, password));
  }

  @Test
  public void testLogin_Successful() {
    long id = 1L;
    String password = "strongPassword123!";

    Officer officer = Officer.builder().firstName("John").id(id).build();
    officer.setPasswordHash(hashPassword(password));

    Mockito.when(officerRepository.findById(id)).thenReturn(Optional.of(officer));
    Mockito.when(
            officerRepository.updateLastLoginById(Mockito.any(Instant.class), Mockito.anyLong()))
        .thenAnswer(
            invocation -> {
              officer.setLastLogin(invocation.getArgument(0));
              return 1;
            });

    String token = officerService.login(id, password);
    assertThat(token).isNotNull();
    assertThat(officer.getLastLogin()).isNotNull();
    assertThat(officer.getLastLogin()).isBetween(Instant.now().minusSeconds(1), Instant.now());
  }
}

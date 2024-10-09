package in.ac.vitap.cse1005.railmadad.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTests {

  @Mock private CustomerRepository customerRepository;
  @Mock private OfficerRepository officerRepository;
  @InjectMocks private AuthService authService;

  @Test
  public void testAuthenticate_WithInvalidToken() {
    assertThrows(Exception.class, () -> authService.authenticate("invalid-token"));
  }

  @Test
  public void testAuthenticate_WithExpiredToken() {
    String token =
        AuthTokenUtils.generateTokenFromUserClaims(
            UserClaims.builder().id("1").role(UserRole.OFFICER).build(), 0);
    assertThrows(ExpiredJwtException.class, () -> authService.authenticate(token));
  }

  @Test
  public void testAuthenticate_WhenCustomerDNE() {
    Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
    String token =
        AuthTokenUtils.generateTokenFromUserClaims(
            UserClaims.builder().id("random-uuid").role(UserRole.CUSTOMER).build(), 10000);

    assertThrows(NoSuchElementException.class, () -> authService.authenticate(token));
  }

  @Test
  public void testAuthenticate_WhenOfficerDNE() {
    String token =
        AuthTokenUtils.generateTokenFromUserClaims(
            UserClaims.builder().id("1").role(UserRole.OFFICER).build(), 10000);

    assertThrows(NoSuchElementException.class, () -> authService.authenticate(token));
  }

  @Test
  public void testAuthenticate_ForCustomerSuccessful() {
    Customer customer =
        Customer.builder().id("random-uuid").firstName("John").phoneNumber(1234567890L).build();
    Mockito.when(customerRepository.findById("random-uuid")).thenReturn(Optional.of(customer));

    String token =
        AuthTokenUtils.generateTokenFromUserClaims(
            UserClaims.builder().id(customer.getId()).role(UserRole.CUSTOMER).build(), 1000);

    UserClaims userClaims = authService.authenticate(token);
    assertEquals(customer.getId(), userClaims.getId());
    assertEquals(UserRole.CUSTOMER, userClaims.getRole());
  }

  @Test
  public void testAuthenticate_ForOfficerSuccessful() {
    Officer officer = Officer.builder().id(1L).firstName("John").build();
    Mockito.when(officerRepository.findById(1L)).thenReturn(Optional.of(officer));

    String token =
        AuthTokenUtils.generateTokenFromUserClaims(
            UserClaims.builder().id(String.valueOf(officer.getId())).role(UserRole.OFFICER).build(),
            1000);

    UserClaims userClaims = authService.authenticate(token);
    assertEquals(String.valueOf(officer.getId()), userClaims.getId());
    assertEquals(UserRole.OFFICER, userClaims.getRole());
  }
}

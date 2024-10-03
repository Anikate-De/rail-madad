package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.getUserClaimsFromToken;

import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class for handling authentication logic. */
@Service
public class AuthService {
  private final CustomerRepository customerRepository;
  private final OfficerRepository officerRepository;

  /**
   * Constructs an AuthService with the specified repositories.
   *
   * @param customerRepository the repository for customer data
   * @param officerRepository the repository for officer data
   */
  @Autowired
  public AuthService(CustomerRepository customerRepository, OfficerRepository officerRepository) {
    this.customerRepository = customerRepository;
    this.officerRepository = officerRepository;
  }

  /**
   * Authenticates a user based on the provided token.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * AuthService authService = new AuthService(customerRepository, officerRepository);
   * UserClaims userClaims = authService.authenticate(token);
   * }</pre>
   *
   * @param token the JWT token containing user claims
   * @return the user claims extracted from the token
   * @throws NoSuchElementException if the user is not found in the respective repository
   */
  public UserClaims authenticate(String token) {
    UserClaims userClaims = getUserClaimsFromToken(token);
    return switch (userClaims.getRole()) {
      case CUSTOMER -> {
        customerRepository.findById(userClaims.getId()).orElseThrow();
        yield userClaims;
      }
      case OFFICER -> {
        officerRepository.findById(Long.valueOf(userClaims.getId())).orElseThrow();
        yield userClaims;
      }
    };
  }
}

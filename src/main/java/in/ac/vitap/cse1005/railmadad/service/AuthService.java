package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.getUserClaimsFromToken;

import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final CustomerRepository customerRepository;

  @Autowired
  public AuthService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public UserClaims authenticate(String token) {
    UserClaims userClaims = getUserClaimsFromToken(token);
    return switch (userClaims.getRole()) {
      case CUSTOMER -> {
        customerRepository.findById(userClaims.getId()).orElseThrow();
        yield userClaims;
      }
      case OFFICER ->
          // TODO: Implement officer authentication from repository
          userClaims;
    };
  }
}

package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.getUserClaimsFromToken;

import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final CustomerRepository customerRepository;
  private final OfficerRepository officerRepository;

  @Autowired
  public AuthService(CustomerRepository customerRepository, OfficerRepository officerRepository) {
    this.customerRepository = customerRepository;
    this.officerRepository = officerRepository;
  }

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

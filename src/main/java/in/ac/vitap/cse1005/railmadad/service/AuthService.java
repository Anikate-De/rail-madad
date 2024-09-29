package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.getIdFromToken;

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

  public String authenticateCustomer(String token) {
    String customerId = getIdFromToken(token);
    customerRepository.findById(customerId).orElseThrow();
    return customerId;
  }
}

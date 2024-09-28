package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.generateTokenWithId;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.checkPasswordStrength;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.hashPassword;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.matchPassword;

import in.ac.vitap.cse1005.railmadad.domain.Customer;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public void signup(Customer customer, String password) {
    if (customer.getFirstName() == null || customer.getPhoneNumber() == 0L || password == null) {
      throw new IncompleteDetailsException();
    }

    checkPasswordStrength(password);
    customer.setPasswordHash(hashPassword(password));

    try {
      customerRepository.save(customer);
    } catch (DataIntegrityViolationException e) {
      throw new EntityExistsException();
    }
  }

  public String login(long phoneNumber, String password) {
    if (phoneNumber == 0 || password == null) {
      throw new IncompleteDetailsException();
    }

    Optional<Customer> customer = customerRepository.findByPhoneNumber(phoneNumber);

    if (customer.isEmpty()) {
      throw new EntityNotFoundException();
    }

    if (!matchPassword(password, customer.get().getPasswordHash())) {
      throw new PasswordMismatchException();
    }

    return generateTokenWithId(customer.get().getId(), 60 * 60 * 1000);
  }
}

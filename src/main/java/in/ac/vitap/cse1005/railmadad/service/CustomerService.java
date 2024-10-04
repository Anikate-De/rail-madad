package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.AuthTokenUtils.generateTokenFromUserClaims;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.checkPasswordStrength;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.hashPassword;
import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.matchPassword;

import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.domain.model.UserClaims;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/** Service class for handling customer-related operations. */
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  /**
   * Constructs a CustomerService with the specified repository.
   *
   * @param customerRepository the repository for customer data
   */
  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  /**
   * Signs up a new customer with the provided details.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Customer customer = new Customer();
   * customerService.signup(customer, "password");
   * }</pre>
   *
   * @param customer the customer to be signed up
   * @param password the password for the customer
   * @throws IncompleteDetailsException if any of the required details are missing
   * @throws EntityExistsException if the customer already exists
   * @throws WeakPasswordException if the password is weak
   */
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

  /**
   * Logs in a customer with the provided phone number and password.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * String token = customerService.login(1234567890L, "password");
   * }</pre>
   *
   * @param phoneNumber the phone number of the customer
   * @param password the password of the customer
   * @return a JWT token for the authenticated customer
   * @throws IncompleteDetailsException if any of the required details are missing
   * @throws EntityNotFoundException if the customer is not found
   * @throws PasswordMismatchException if the password does not match
   */
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

    customerRepository.updateLastLoginById(
        Instant.ofEpochMilli(System.currentTimeMillis()), customer.get().getId());

    return generateTokenFromUserClaims(
        UserClaims.builder().id(customer.get().getId()).role(UserRole.CUSTOMER).build(),
        60 * 60 * 1000);
  }
}

package in.ac.vitap.cse1005.railmadad.service;

import static in.ac.vitap.cse1005.railmadad.utils.PasswordUtils.hashPassword;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import jakarta.persistence.EntityExistsException;
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
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTests {

  @Mock private CustomerRepository customerRepository;
  @InjectMocks private CustomerService customerService;

  @Test
  public void testSignUp_WithIncompleteDetails() {
    Customer customer = new Customer();
    String password = "password";

    assertThrows(
        IncompleteDetailsException.class, () -> customerService.signup(customer, password));
    customer.setPhoneNumber(1234567890L);

    assertThrows(
        IncompleteDetailsException.class, () -> customerService.signup(customer, password));

    customer.setFirstName("John");
    customer.setPhoneNumber(0L);
    assertThrows(
        IncompleteDetailsException.class, () -> customerService.signup(customer, password));
  }

  @Test
  public void testSignUp_WithNoPassword() {
    Customer customer = Customer.builder().firstName("John").phoneNumber(1234567890L).build();

    assertThrows(IncompleteDetailsException.class, () -> customerService.signup(customer, null));
  }

  @Test
  public void testSignUp_WithWeakPassword() {
    Customer customer = Customer.builder().firstName("John").phoneNumber(1234567890L).build();
    String password = "password";

    assertThrows(WeakPasswordException.class, () -> customerService.signup(customer, password));
  }

  @Test
  public void testSignUp_WithExistingPhoneNumber() {
    Customer customer = Customer.builder().firstName("John").phoneNumber(1234567890L).build();
    String password = "strongPassword123!";

    List<Customer> customers = new ArrayList<>();
    customers.add(Customer.builder().firstName("Jane").phoneNumber(1234567890L).build());

    Mockito.when(customerRepository.save(Mockito.any(Customer.class)))
        .thenAnswer(
            invocation -> {
              Customer savedCustomer = invocation.getArgument(0);
              if (customers.stream()
                  .anyMatch(c -> c.getPhoneNumber() == savedCustomer.getPhoneNumber())) {
                throw new DataIntegrityViolationException("");
              }
              customers.add(savedCustomer);
              return savedCustomer;
            });

    assertThrows(EntityExistsException.class, () -> customerService.signup(customer, password));
  }

  @Test
  public void testSignUp_Successful() {
    Customer customer = Customer.builder().firstName("John").phoneNumber(1234567890L).build();
    String password = "strongPassword123!";

    List<Customer> customers = new ArrayList<>();
    Mockito.when(customerRepository.save(Mockito.any(Customer.class)))
        .thenAnswer(
            invocation -> {
              Customer savedCustomer = invocation.getArgument(0);
              customers.add(savedCustomer);
              return savedCustomer;
            });
    Mockito.when(customerRepository.findAll()).thenReturn(customers);

    customerService.signup(customer, password);

    List<Customer> fetchedCustomers = (List<Customer>) customerRepository.findAll();
    assertEquals(1, fetchedCustomers.size());
    assertEquals("John", fetchedCustomers.getFirst().getFirstName());
    assertEquals(1234567890L, fetchedCustomers.getFirst().getPhoneNumber());
  }

  @Test
  public void testLogin_WithIncompleteDetails() {
    long phoneNumber = 1234567890L;
    String password = "password";

    assertThrows(IncompleteDetailsException.class, () -> customerService.login(0L, password));
    assertThrows(IncompleteDetailsException.class, () -> customerService.login(phoneNumber, null));
  }

  @Test
  public void testLogin_WithMissingPhoneNumber() {
    long phoneNumber = 1234567890L;
    String password = "password";

    Mockito.when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> customerService.login(phoneNumber, password));
  }

  @Test
  public void testLogin_WithMismatchedPassword() {
    long phoneNumber = 1234567890L;
    String password = "password";

    Customer customer = Customer.builder().firstName("John").phoneNumber(phoneNumber).build();
    customer.setPasswordHash("hashedPassword");

    Mockito.when(customerRepository.findByPhoneNumber(phoneNumber))
        .thenReturn(Optional.of(customer));

    assertThrows(
        PasswordMismatchException.class, () -> customerService.login(phoneNumber, password));
  }

  @Test
  public void testLogin_Successful() {
    long phoneNumber = 1234567890L;
    String password = "strongPassword123!";

    Customer customer =
        Customer.builder().firstName("John").phoneNumber(phoneNumber).id("random_uuid").build();
    customer.setPasswordHash(hashPassword(password));

    Mockito.when(customerRepository.findByPhoneNumber(phoneNumber))
        .thenReturn(Optional.of(customer));
    Mockito.when(
            customerRepository.updateLastLoginById(Mockito.any(Instant.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              customer.setLastLogin(invocation.getArgument(0));
              return 1;
            });

    String token = customerService.login(phoneNumber, password);
    assertThat(token).isNotNull();
    assertThat(customer.getLastLogin()).isNotNull();
    assertThat(customer.getLastLogin()).isBetween(Instant.now().minusSeconds(1), Instant.now());
  }
}

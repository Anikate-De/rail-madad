package in.ac.vitap.cse1005.railmadad.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositoryIntegrationTests {

  private final CustomerRepository underTest;

  @Autowired
  public CustomerRepositoryIntegrationTests(CustomerRepository underTest) {
    this.underTest = underTest;
  }

  @Test
  public void testSave() {
    Customer customer = Customer.builder().firstName("John").phoneNumber(1234567890L).build();

    underTest.save(customer);

    List<Customer> customers = (List<Customer>) underTest.findAll();
    Customer received = customers.getFirst();

    assertThat(customers).hasSize(1);
    assertThat(received.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(received.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
    assertThat(received.getId()).isNotNull();
    assertThat(received.getLastLogin()).isNull();
    assertThat(received.getDateRegistered())
        .isBetween(Instant.now().minusSeconds(1), Instant.now());
  }

  @Test
  public void testFindByPhoneNumber() {
    Customer customerA = Customer.builder().firstName("Jack").phoneNumber(9876543210L).build();
    Customer customerB = Customer.builder().firstName("Jill").phoneNumber(9876543211L).build();

    underTest.save(customerA);
    underTest.save(customerB);

    Optional<Customer> received = underTest.findByPhoneNumber(9876543211L);

    assertThat(received).isPresent();
    assertThat(received.get().getFirstName()).isEqualTo(customerB.getFirstName());
    assertThat(received.get().getPhoneNumber()).isEqualTo(customerB.getPhoneNumber());
    assertThat(received.get().getId()).isNotNull();
    assertThat(received.get().getLastLogin()).isNull();
    assertThat(received.get().getDateRegistered())
        .isBetween(Instant.now().minusSeconds(1), Instant.now());
  }

  @Test
  public void testUpdateLastLoginById() {
    Customer customer = Customer.builder().firstName("John").phoneNumber(1234567890L).build();

    Customer savedCustomer = underTest.save(customer);

    customer.setId(savedCustomer.getId());

    Instant lastLogin = Instant.now();
    underTest.updateLastLoginById(lastLogin, customer.getId());

    Optional<Customer> received = underTest.findById(customer.getId());

    assertThat(received).isPresent();
    assertThat(received.get().getLastLogin())
        .isBetween(lastLogin.minusMillis(100), lastLogin.plusMillis(100));
  }
}

package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Repository interface for handling CRUD operations on Customer entities. */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

  /**
   * Finds a customer by their phone number.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Optional<Customer> customer = customerRepository.findByPhoneNumber(1234567890L);
   * }</pre>
   *
   * @param phoneNumber the phone number of the customer
   * @return an Optional containing the customer if found, or empty if not found
   */
  Optional<Customer> findByPhoneNumber(@NonNull long phoneNumber);

  /**
   * Updates the last login time of a customer by their ID.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * customerRepository.updateLastLoginById(Instant.now(), "customerId");
   * }</pre>
   *
   * @param lastLogin the last login time to be set
   * @param id the ID of the customer
   * @return the number of entities updated
   */
  @Transactional
  @Modifying
  @Query("update Customer c set c.lastLogin = ?1 where c.id = ?2")
  int updateLastLoginById(@NonNull Instant lastLogin, @NonNull String id);
}

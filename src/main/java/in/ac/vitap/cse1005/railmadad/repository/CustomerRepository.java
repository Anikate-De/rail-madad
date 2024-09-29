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

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
  Optional<Customer> findByPhoneNumber(@NonNull long phoneNumber);

  @Transactional
  @Modifying
  @Query("update Customer c set c.lastLogin = ?1 where c.id = ?2")
  int updateLastLoginById(@NonNull Instant lastLogin, @NonNull String id);
}

package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.Customer;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
  Optional<Customer> findByPhoneNumber(@NonNull long phoneNumber);
}

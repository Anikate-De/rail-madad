package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import java.time.Instant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Repository interface for handling CRUD operations on Officer entities. */
@Repository
public interface OfficerRepository extends CrudRepository<Officer, Long> {

  /**
   * Updates the last login time of an officer by their ID.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * officerRepository.updateLastLoginById(Instant.now(), 1L);
   * }</pre>
   *
   * @param lastLogin the last login time to be set
   * @param id the ID of the officer
   * @return the number of entities updated
   */
  @Transactional
  @Modifying
  @Query("update Officer o set o.lastLogin = ?1 where o.id = ?2")
  int updateLastLoginById(@NonNull Instant lastLogin, @NonNull long id);
}

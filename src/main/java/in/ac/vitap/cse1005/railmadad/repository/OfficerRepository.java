package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import java.time.Instant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OfficerRepository extends CrudRepository<Officer, Long> {
  @Transactional
  @Modifying
  @Query("update Officer o set o.lastLogin = ?1 where o.id = ?2")
  int updateLastLoginById(@NonNull Instant lastLogin, @NonNull long id);
}

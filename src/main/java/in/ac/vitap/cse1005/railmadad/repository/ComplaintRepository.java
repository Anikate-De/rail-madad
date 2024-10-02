package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {
  @Query("select c from Complaint c where c.customer.id = ?1")
  List<Complaint> findByCustomer_Id(@NonNull String id);

  @Query("select c from Complaint c where c.officer.id = ?1")
  List<Complaint> findByOfficer_Id(@NonNull long id);
}

package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.Complaint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {}

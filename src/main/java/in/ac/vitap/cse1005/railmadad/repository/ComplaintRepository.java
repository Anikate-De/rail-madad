package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/** Repository interface for handling CRUD operations on Complaint entities. */
@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {

  /**
   * Finds complaints by the customer ID.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Complaint> complaints = complaintRepository.findByCustomer_Id("customerId");
   * }</pre>
   *
   * @param id the ID of the customer
   * @return a list of complaints associated with the specified customer ID
   */
  @Query("select c from Complaint c where c.customer.id = ?1")
  List<Complaint> findByCustomer_Id(@NonNull String id);

  /**
   * Finds complaints by the officer ID.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Complaint> complaints = complaintRepository.findByOfficer_Id(12345L);
   * }</pre>
   *
   * @param id the ID of the officer
   * @return a list of complaints associated with the specified officer ID
   */
  @Query("select c from Complaint c where c.officer.id = ?1")
  List<Complaint> findByOfficer_Id(@NonNull long id);

  /**
   * Finds complaints by their status.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Complaint> pendingComplaints = complaintRepository.findByStatus(ComplaintStatus.PENDING);
   * }</pre>
   *
   * @param status the status of the complaints to find
   * @return a list of complaints with the specified status
   */
  @Query("select c from Complaint c where c.status = ?1")
  List<Complaint> findByStatus(@NonNull ComplaintStatus status);
}

package in.ac.vitap.cse1005.railmadad.service;

import in.ac.vitap.cse1005.railmadad.domain.entity.Category;
import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintCategory;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import in.ac.vitap.cse1005.railmadad.repository.CategoryRepository;
import in.ac.vitap.cse1005.railmadad.repository.ComplaintRepository;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import in.ac.vitap.cse1005.railmadad.utils.ContentCategorizationUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service for categorizing pending complaints.
 *
 * <p>This service periodically categorizes pending complaints and assigns them to officers.
 */
@Slf4j
@Service
public class CategorizationService {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  private final ComplaintRepository complaintRepository;
  private final CategoryRepository categoryRepository;
  private final OfficerRepository officerRepository;

  /**
   * Constructs a CategorizationService with the specified repositories.
   *
   * @param complaintRepository the repository for complaint operations
   * @param categoryRepository the repository for category operations
   * @param officerRepository the repository for officer operations
   */
  @Autowired
  public CategorizationService(
      ComplaintRepository complaintRepository,
      CategoryRepository categoryRepository,
      OfficerRepository officerRepository) {
    this.complaintRepository = complaintRepository;
    this.categoryRepository = categoryRepository;
    this.officerRepository = officerRepository;
  }

  /**
   * Scheduled task for categorizing pending complaints.
   *
   * <p>This method is executed at a fixed rate to categorize pending complaints and assign them to
   * officers.
   */
  @Scheduled(fixedRate = 15000)
  public void categorizePendingComplaints() {
    log.info("Batch Categorization Execution Time: {}", dateFormat.format(new Date()));

    // Retrieve all pending complaints
    List<Complaint> pendingComplaints = complaintRepository.findByStatus(ComplaintStatus.PENDING);
    AtomicInteger countComplaintsCategorized = new AtomicInteger();

    pendingComplaints.forEach(
        complaint -> {
          // Determine the category of the complaint based on its title and summary
          ComplaintCategory categoryEnum =
              ContentCategorizationUtils.getCategory(
                  "%s %s".formatted(complaint.getTitle(), complaint.getSummary()));

          // Find the corresponding category entity
          Optional<Category> category =
              categoryRepository.findByNameIgnoreCase(categoryEnum.getCategoryName());

          if (category.isEmpty()) {
            return;
          }

          // Retrieve officers from the relevant department
          List<Officer> officers =
              officerRepository.findByDepartment_Id(category.get().getDepartment().getId());

          // Randomly assign an officer to the complaint
          Officer officer = officers.get(new Random().nextInt(officers.size()));

          // Update the complaint with the category, officer, and status
          complaint.setCategory(category.get());
          complaint.setOfficer(officer);
          complaint.setStatus(ComplaintStatus.IN_PROGRESS);

          // Save the updated complaint
          complaintRepository.save(complaint);
          countComplaintsCategorized.getAndIncrement();
        });

    log.info(
        "Batch Categorization Finished. Total PENDING Complaints received: {}. Complaints"
            + " categorized: {}",
        pendingComplaints.size(),
        countComplaintsCategorized.get());
  }
}

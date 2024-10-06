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
import in.ac.vitap.cse1005.railmadad.utils.LogWriterUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
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
  private Directory index;
  private Analyzer analyzer;

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

    try {
      initSearchIndex();
    } catch (IOException e) {
      log.error("Error initializing search index: {}", e.getMessage());
    }
  }

  /**
   * Initializes the search index with keywords for categorization.
   *
   * @throws IOException if an I/O error occurs during index initialization
   */
  public void initSearchIndex() throws IOException {
    index = new ByteBuffersDirectory();
    analyzer = new StandardAnalyzer();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    IndexWriter writer = new IndexWriter(index, config);

    // Load keywords and add them to the index
    for (Map.Entry<ComplaintCategory, List<String>> entry :
        ContentCategorizationUtils.loadKeywords().entrySet()) {
      ComplaintCategory category = entry.getKey();
      List<String> keywords = entry.getValue();
      for (String keyword : keywords) {
        Document doc = new Document();
        doc.add(new StringField("category", category.getCategoryName(), Field.Store.YES));
        doc.add(new TextField("keyword", keyword, Field.Store.YES));
        writer.addDocument(doc);
      }
    }
    writer.close();
  }

  /**
   * Scheduled task for categorizing pending complaints.
   *
   * <p>This method is executed at a fixed rate to categorize pending complaints and assign them to
   * officers.
   */
  @Scheduled(fixedRate = 15000)
  public void categorizePendingComplaints() {
    if (index == null || analyzer == null) {
      log.error("Batch Categorization Failed: Search index not initialized.");
      return;
    }

    log.info("Batch Categorization Execution Time: {}", dateFormat.format(new Date()));

    // Retrieve all pending complaints
    List<Complaint> pendingComplaints = complaintRepository.findByStatus(ComplaintStatus.PENDING);
    AtomicInteger countComplaintsCategorized = new AtomicInteger();

    pendingComplaints.forEach(
        complaint -> {
          // Determine the category of the complaint based on its title and summary
          ComplaintCategory categoryEnum;
          try {
            categoryEnum =
                ContentCategorizationUtils.getCategory(
                    "%s %s".formatted(complaint.getTitle(), complaint.getSummary()),
                    index,
                    analyzer);
          } catch (Exception e) {
            categoryEnum = ComplaintCategory.MISCELLANEOUS;
          }

          // Find the corresponding category entity
          Optional<Category> category =
              categoryRepository.findByNameIgnoreCase(categoryEnum.getCategoryName());

          if (category.isEmpty()) {
            return;
          }

          // Retrieve officers from the relevant department
          List<Officer> officers =
              officerRepository.findByDepartment_Id(category.get().getDepartment().getId());

          // Assign officer with the least number of in-progress complaints
          Officer officer =
              officers.stream()
                  .min(
                      Comparator.comparingInt(
                          o ->
                              o.getComplaints().stream()
                                  .takeWhile(c -> c.getStatus() == ComplaintStatus.IN_PROGRESS)
                                  .toList()
                                  .size()))
                  .orElse(officers.get(new Random().nextInt(officers.size())));

          // Update the complaint with the category, officer, and status
          complaint.setCategory(category.get());
          complaint.setOfficer(officer);
          complaint.setStatus(ComplaintStatus.IN_PROGRESS);

          // Save the updated complaint
          complaintRepository.save(complaint);
          countComplaintsCategorized.getAndIncrement();

          String logMessage =
              "Complaint with Title: "
                  + complaint.getTitle()
                  + "; Summary: "
                  + complaint.getSummary()
                  + " categorized into "
                  + categoryEnum.getCategoryName()
                  + "; Assigned to Officer with ID: "
                  + officer.getId();
          LogWriterUtils.writeLog("logs/complaint_categorization", logMessage);
          log.info(logMessage);
        });

    log.info(
        "Batch Categorization Finished. Total PENDING Complaints received: {}. Complaints"
            + " categorized: {}",
        pendingComplaints.size(),
        countComplaintsCategorized.get());
  }
}

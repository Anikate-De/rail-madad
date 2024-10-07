package in.ac.vitap.cse1005.railmadad.repository;

import static org.assertj.core.api.Assertions.assertThat;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ComplaintRepositoryIntegrationTests {
  private final ComplaintRepository complaintRepository;
  private final CustomerRepository customerRepository;
  private final OfficerRepository officerRepository;
  private Customer customer;
  private Officer officer;

  @Autowired
  public ComplaintRepositoryIntegrationTests(
      ComplaintRepository complaintRepository,
      CustomerRepository customerRepository,
      OfficerRepository officerRepository) {
    this.complaintRepository = complaintRepository;
    this.customerRepository = customerRepository;
    this.officerRepository = officerRepository;
  }

  @BeforeEach
  public void setUp() {
    customer = Customer.builder().firstName("Test").build();
    customer = customerRepository.save(customer);

    officer = Officer.builder().firstName("Test").build();
    officer = officerRepository.save(officer);
  }

  @Test
  public void testSave() {
    Complaint complaint = new Complaint();
    complaint.setTitle("Test Complaint");
    complaint.setSummary("Test Summary");
    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setCustomer(customer);
    complaint.setOfficer(officer);

    Complaint savedComplaint = complaintRepository.save(complaint);

    assertThat(savedComplaint).isNotNull();
    assertThat(savedComplaint.getId()).isNotNull();
    assertThat(savedComplaint.getTitle()).isEqualTo("Test Complaint");
    assertThat(savedComplaint.getCustomer().getId()).isEqualTo(customer.getId());
    assertThat(savedComplaint.getOfficer().getId()).isEqualTo(officer.getId());
  }

  @Test
  public void testFindByCustomer_Id() {
    Complaint complaint = new Complaint();
    complaint.setTitle("Test Complaint");
    complaint.setSummary("Test Summary");
    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setCustomer(customer);
    complaint.setOfficer(officer);
    complaintRepository.save(complaint);

    List<Complaint> complaints = complaintRepository.findByCustomer_Id(customer.getId());

    assertThat(complaints).isNotEmpty();
    assertThat(complaints.get(0).getCustomer().getId()).isEqualTo(customer.getId());
  }

  @Test
  public void testFindByOfficer_Id() {
    Complaint complaint = new Complaint();
    complaint.setTitle("Test Complaint");
    complaint.setSummary("Test Summary");
    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setCustomer(customer);
    complaint.setOfficer(officer);
    complaintRepository.save(complaint);

    List<Complaint> complaints = complaintRepository.findByOfficer_Id(officer.getId());

    assertThat(complaints).isNotEmpty();
    assertThat(complaints.get(0).getOfficer().getId()).isEqualTo(officer.getId());
  }

  @Test
  public void testFindByStatus() {
    Complaint complaint = new Complaint();
    complaint.setTitle("Test Complaint");
    complaint.setSummary("Test Summary");
    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setCustomer(customer);
    complaint.setOfficer(officer);
    complaintRepository.save(complaint);

    List<Complaint> complaints = complaintRepository.findByStatus(ComplaintStatus.PENDING);

    assertThat(complaints).isNotEmpty();
    assertThat(complaints.get(0).getStatus()).isEqualTo(ComplaintStatus.PENDING);
  }
}

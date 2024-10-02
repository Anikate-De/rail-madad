package in.ac.vitap.cse1005.railmadad.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.domain.entity.Media;
import in.ac.vitap.cse1005.railmadad.domain.entity.Message;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import in.ac.vitap.cse1005.railmadad.exceptions.AccessDeniedException;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.repository.ComplaintRepository;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import in.ac.vitap.cse1005.railmadad.repository.MediaRepository;
import in.ac.vitap.cse1005.railmadad.repository.MessageRepository;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class ComplaintServiceTests {

  @Mock private ComplaintRepository complaintRepository;
  @Mock private CustomerRepository customerRepository;
  @Mock private OfficerRepository officerRepository;
  @Mock private MediaRepository mediaRepository;
  @Mock private MessageRepository messageRepository;
  @InjectMocks private ComplaintService underTest;

  @Test
  public void testGetFiledComplaints_NoComplaints() {
    Mockito.when(complaintRepository.findByCustomer_Id(Mockito.anyString())).thenReturn(List.of());

    List<Complaint> complaints = underTest.getFiledComplaints("customer-id");
    assertThat(complaints).isEmpty();
  }

  @Test
  public void testGetFiledComplaints_All() {
    List<Complaint> complaints =
        List.of(
            Complaint.builder().customer(Customer.builder().id("cusA").build()).build(),
            Complaint.builder().customer(Customer.builder().id("cusA").build()).build());

    Mockito.when(complaintRepository.findByCustomer_Id("customer-id")).thenReturn(complaints);

    List<Complaint> actualComplaints = underTest.getFiledComplaints("customer-id");
    assertThat(actualComplaints).hasSize(2);
    assertThat(actualComplaints).containsAll(complaints);
  }

  @Test
  public void testGetAssignedComplaints_NoComplaints() {
    Mockito.when(complaintRepository.findByOfficer_Id(Mockito.anyLong())).thenReturn(List.of());

    List<Complaint> complaints = underTest.getAssignedComplaints(1L);
    assertThat(complaints).isEmpty();
  }

  @Test
  public void testGetAssignedComplaints_All() {
    List<Complaint> complaints =
        List.of(
            Complaint.builder().officer(Officer.builder().id(1L).build()).build(),
            Complaint.builder().officer(Officer.builder().id(1L).build()).build());

    Mockito.when(complaintRepository.findByOfficer_Id(1L)).thenReturn(complaints);

    List<Complaint> actualComplaints = underTest.getAssignedComplaints(1L);
    assertThat(actualComplaints).hasSize(2);
    assertThat(actualComplaints).containsAll(complaints);
  }

  @Test
  public void testPostComplaint_IncompleteComplaintDetails() {
    Mockito.when(customerRepository.findById("cusA"))
        .thenReturn(java.util.Optional.of(Customer.builder().id("cusA").build()));

    Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
        .thenAnswer(
            invocation -> {
              invocation.getArgument(0);
              if (((Complaint) invocation.getArgument(0)).getTitle() == null) {
                throw new DataIntegrityViolationException("");
              }
              return invocation.getArgument(0);
            });

    Assertions.assertThrows(
        IncompleteDetailsException.class,
        () -> underTest.postComplaint("cusA", Complaint.builder().build()));
  }

  @Test
  public void testPostComplaint_IncompleteMediaDetails() {
    Mockito.when(customerRepository.findById("cusA"))
        .thenReturn(java.util.Optional.of(Customer.builder().id("cusA").build()));

    Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
        .thenReturn(Complaint.builder().build());

    Mockito.when(mediaRepository.saveAll(Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              invocation.getArgument(0);
              List<Media> mediaList = invocation.getArgument(0);
              if (mediaList.stream().anyMatch(media -> media.getData() == null)) {
                throw new DataIntegrityViolationException("");
              }
              return invocation.getArgument(0);
            });

    Assertions.assertThrows(
        IncompleteDetailsException.class,
        () ->
            underTest.postComplaint(
                "cusA",
                Complaint.builder()
                    .title("Test")
                    .mediaList(
                        List.of(
                            Media.builder().build(),
                            Media.builder().data(new byte[] {1, 12, 123}).build()))
                    .build()));
  }

  @Test
  public void testPostComplaint_Successful() {
    Mockito.when(customerRepository.findById("cusA"))
        .thenReturn(java.util.Optional.of(Customer.builder().id("cusA").build()));

    Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
        .thenAnswer(
            invocation -> {
              invocation.getArgument(0);
              if (((Complaint) invocation.getArgument(0)).getTitle() == null) {
                throw new DataIntegrityViolationException("");
              }
              return invocation.getArgument(0);
            });

    Mockito.when(mediaRepository.saveAll(Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              invocation.getArgument(0);
              List<Media> mediaList = invocation.getArgument(0);
              if (mediaList.stream().anyMatch(media -> media.getData() == null)) {
                throw new DataIntegrityViolationException("");
              }
              return invocation.getArgument(0);
            });

    Complaint complaint =
        underTest.postComplaint(
            "cusA",
            Complaint.builder()
                .title("Test")
                .mediaList(
                    List.of(
                        Media.builder().data(new byte[] {4, 45}).build(),
                        Media.builder().data(new byte[] {1, 12, 123}).build()))
                .build());

    assertThat(complaint).isNotNull();
    assertThat(complaint.getTitle()).isEqualTo("Test");
    assertThat(complaint.getMediaList()).hasSize(2);
    assertThat(complaint.getMediaList().get(0).getData()).containsExactly((byte) 4, (byte) 45);
    assertThat(complaint.getMediaList().get(1).getData())
        .containsExactly((byte) 1, (byte) 12, (byte) 123);
  }

  @Test
  public void testUpdateComplaintStatus_MissingStatus() {
    Assertions.assertThrows(
        IncompleteDetailsException.class, () -> underTest.updateComplaintStatus(1L, null, 1L));
  }

  @Test
  public void testUpdateComplaintStatus_MissingComplaintId() {
    Assertions.assertThrows(
        IncompleteDetailsException.class,
        () -> underTest.updateComplaintStatus(null, ComplaintStatus.CLOSED, 1L));
  }

  @Test
  public void testUpdateComplaintStatus_ComplaintDNE() {
    Mockito.when(complaintRepository.findById(1L)).thenReturn(java.util.Optional.empty());

    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> underTest.updateComplaintStatus(1L, ComplaintStatus.CLOSED, 1L));
  }

  @Test
  public void testUpdateComplaintStatus_OfficerNotAssigned() {
    Mockito.when(complaintRepository.findById(1L))
        .thenReturn(java.util.Optional.of(Complaint.builder().officer(null).build()));

    Assertions.assertThrows(
        AccessDeniedException.class,
        () -> underTest.updateComplaintStatus(1L, ComplaintStatus.CLOSED, 1L));
  }

  @Test
  public void testUpdateComplaintStatus_MismatchedOfficerId() {
    Mockito.when(complaintRepository.findById(1L))
        .thenReturn(
            java.util.Optional.of(
                Complaint.builder().officer(Officer.builder().id(2L).build()).build()));

    Assertions.assertThrows(
        AccessDeniedException.class,
        () -> underTest.updateComplaintStatus(1L, ComplaintStatus.CLOSED, 1L));
  }

  @Test
  public void testUpdateComplaintStatus_Successful() {
    Mockito.when(complaintRepository.findById(1L))
        .thenReturn(
            java.util.Optional.of(
                Complaint.builder().officer(Officer.builder().id(1L).build()).build()));

    Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
        .thenAnswer(
            invocation -> {
              invocation.getArgument(0);
              return invocation.getArgument(0);
            });

    Complaint complaint = underTest.updateComplaintStatus(1L, ComplaintStatus.CLOSED, 1L);
    assertThat(complaint).isNotNull();
    assertThat(complaint.getStatus()).isEqualTo(ComplaintStatus.CLOSED);
  }

  @Test
  public void testAddMessage_IncompleteMessageDetails() {
    Assertions.assertThrows(
        IncompleteDetailsException.class,
        () -> underTest.addMessage(1L, Message.builder().build(), 1L));
  }

  @Test
  public void testAddMessage_MissingComplaintId() {
    Assertions.assertThrows(
        IncompleteDetailsException.class,
        () -> underTest.addMessage(null, Message.builder().body("Test").build(), 1L));
  }

  @Test
  public void testAddMessage_ComplaintDNE() {
    Mockito.when(complaintRepository.findById(1L)).thenReturn(java.util.Optional.empty());

    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> underTest.addMessage(1L, Message.builder().body("Test").build(), 1L));
  }

  @Test
  public void testAddMessage_OfficerNotAssigned() {
    Mockito.when(complaintRepository.findById(1L))
        .thenReturn(java.util.Optional.of(Complaint.builder().officer(null).build()));

    Assertions.assertThrows(
        AccessDeniedException.class,
        () -> underTest.addMessage(1L, Message.builder().body("Test").build(), 1L));
  }

  @Test
  public void testAddMessage_MismatchedOfficerId() {
    Mockito.when(complaintRepository.findById(1L))
        .thenReturn(
            java.util.Optional.of(
                Complaint.builder().officer(Officer.builder().id(2L).build()).build()));

    Assertions.assertThrows(
        AccessDeniedException.class,
        () -> underTest.addMessage(1L, Message.builder().body("Test").build(), 1L));
  }

  @Test
  public void testAddMessage_Successful() {
    Mockito.when(complaintRepository.findById(1L))
        .thenReturn(
            java.util.Optional.of(
                Complaint.builder().officer(Officer.builder().id(1L).build()).build()));

    Mockito.when(officerRepository.findById(1L))
        .thenReturn(java.util.Optional.of(Officer.builder().id(1L).build()));

    Mockito.when(messageRepository.save(Mockito.any(Message.class)))
        .thenAnswer(
            invocation -> {
              invocation.getArgument(0);
              return invocation.getArgument(0);
            });

    Message message = underTest.addMessage(1L, Message.builder().body("Test").build(), 1L);
    assertThat(message).isNotNull();
    assertThat(message.getBody()).isEqualTo("Test");
  }
}

package in.ac.vitap.cse1005.railmadad.service;

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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

  private final ComplaintRepository complaintRepository;
  private final CustomerRepository customerRepository;
  private final OfficerRepository officerRepository;
  private final MediaRepository mediaRepository;
  private final MessageRepository messageRepository;

  @Autowired
  public ComplaintService(
      ComplaintRepository complaintRepository,
      CustomerRepository customerRepository,
      OfficerRepository officerRepository,
      MediaRepository mediaRepository,
      MessageRepository messageRepository) {
    this.complaintRepository = complaintRepository;
    this.customerRepository = customerRepository;
    this.officerRepository = officerRepository;
    this.mediaRepository = mediaRepository;
    this.messageRepository = messageRepository;
  }

  public List<Complaint> getFiledComplaints(String customerId) {
    return complaintRepository.findByCustomer_Id(customerId);
  }

  public List<Complaint> getAssignedComplaints(Long officerId) {
    return complaintRepository.findByOfficer_Id(officerId);
  }

  public Complaint postComplaint(String customerId, Complaint complaint) {
    Customer customer = customerRepository.findById(customerId).orElseThrow();
    complaint.setCustomer(customer);
    List<Media> mediaList = complaint.getMediaList();
    complaint.setMediaList(List.of());

    try {
      Complaint savedComplaint = complaintRepository.save(complaint);

      List<Media> savedMediaList =
          (List<Media>)
              mediaRepository.saveAll(
                  mediaList.stream().peek(media -> media.setComplaint(savedComplaint)).toList());

      complaint.setMediaList(savedMediaList);

      return complaint;
    } catch (DataIntegrityViolationException e) {
      throw new IncompleteDetailsException();
    }
  }

  public Complaint updateComplaintStatus(Long complaintId, ComplaintStatus status, Long officerId) {
    if (complaintId == null || status == null || officerId == null) {
      throw new IncompleteDetailsException();
    }

    Complaint complaint = complaintRepository.findById(complaintId).orElseThrow();
    if (complaint.getOfficer().getId() != officerId) {
      throw new AccessDeniedException();
    }

    complaint.setStatus(status);

    return complaintRepository.save(complaint);
  }

  public Message addMessage(Long complaintId, Message message, Long officerId) {
    if (complaintId == null || message.getBody() == null) {
      throw new IncompleteDetailsException();
    }

    Optional<Complaint> complaint = complaintRepository.findById(complaintId);
    if (complaint.isEmpty()) {
      throw new NoSuchElementException();
    }

    message.setComplaint(complaint.get());

    Optional<Officer> officer = officerRepository.findById(officerId);
    if (officer.isEmpty() || complaint.get().getOfficer().getId() != officerId) {
      throw new AccessDeniedException();
    }

    message.setOfficer(officer.get());

    return messageRepository.save(message);
  }
}

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

/** Service class for handling complaint-related operations. */
@Service
public class ComplaintService {

  private final ComplaintRepository complaintRepository;
  private final CustomerRepository customerRepository;
  private final OfficerRepository officerRepository;
  private final MediaRepository mediaRepository;
  private final MessageRepository messageRepository;

  /**
   * Constructs a ComplaintService with the specified repositories.
   *
   * @param complaintRepository the repository for complaint data
   * @param customerRepository the repository for customer data
   * @param officerRepository the repository for officer data
   * @param mediaRepository the repository for media data
   * @param messageRepository the repository for message data
   */
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

  /**
   * Retrieves the list of complaints filed by a specific customer.
   *
   * @param customerId the ID of the customer
   * @return the list of complaints filed by the customer
   */
  public List<Complaint> getFiledComplaints(String customerId) {
    return complaintRepository.findByCustomer_Id(customerId);
  }

  /**
   * Retrieves the list of complaints assigned to a specific officer.
   *
   * @param officerId the ID of the officer
   * @return the list of complaints assigned to the officer
   */
  public List<Complaint> getAssignedComplaints(Long officerId) {
    return complaintRepository.findByOfficer_Id(officerId);
  }

  /**
   * Posts a new complaint for a specific customer.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Complaint complaint = new Complaint();
   * complaintService.postComplaint(customerId, complaint);
   * }</pre>
   *
   * @param customerId the ID of the customer
   * @param complaint the complaint to be posted
   * @return the saved complaint
   * @throws IncompleteDetailsException if the complaint details are incomplete
   */
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

  /**
   * Updates the status of a specific complaint.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * complaintService.updateComplaintStatus(complaintId, ComplaintStatus.RESOLVED, officerId);
   * }</pre>
   *
   * @param complaintId the ID of the complaint
   * @param status the new status of the complaint
   * @param officerId the ID of the officer updating the status
   * @return the updated complaint
   * @throws IncompleteDetailsException if any of the parameters are null
   * @throws AccessDeniedException if the officer is not authorized to update the complaint
   */
  public Complaint updateComplaintStatus(Long complaintId, ComplaintStatus status, Long officerId) {
    if (complaintId == null || status == null || officerId == null) {
      throw new IncompleteDetailsException();
    }

    Complaint complaint = complaintRepository.findById(complaintId).orElseThrow();
    if (complaint.getOfficer() == null || complaint.getOfficer().getId() != officerId) {
      throw new AccessDeniedException();
    }

    complaint.setStatus(status);

    return complaintRepository.save(complaint);
  }

  /**
   * Adds a message to a specific complaint.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Message message = new Message();
   * complaintService.addMessage(complaintId, message, officerId);
   * }</pre>
   *
   * @param complaintId the ID of the complaint
   * @param message the message to be added
   * @param officerId the ID of the officer adding the message
   * @return the saved message
   * @throws IncompleteDetailsException if the complaint ID or message body is null
   * @throws NoSuchElementException if the complaint is not found
   * @throws AccessDeniedException if the officer is not authorized to add the message
   */
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
    if (officer.isEmpty()
        || complaint.get().getOfficer() == null
        || complaint.get().getOfficer().getId() != officerId) {
      throw new AccessDeniedException();
    }

    message.setOfficer(officer.get());

    return messageRepository.save(message);
  }
}

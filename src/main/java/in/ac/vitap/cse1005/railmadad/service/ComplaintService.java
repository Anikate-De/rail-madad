package in.ac.vitap.cse1005.railmadad.service;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.domain.entity.Media;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import in.ac.vitap.cse1005.railmadad.exceptions.AccessDeniedException;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.repository.ComplaintRepository;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import in.ac.vitap.cse1005.railmadad.repository.MediaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

  private final ComplaintRepository complaintRepository;
  private final CustomerRepository customerRepository;
  private final MediaRepository mediaRepository;

  @Autowired
  public ComplaintService(
      ComplaintRepository complaintRepository,
      CustomerRepository customerRepository,
      MediaRepository mediaRepository) {
    this.complaintRepository = complaintRepository;
    this.customerRepository = customerRepository;
    this.mediaRepository = mediaRepository;
  }

  public List<Complaint> getComplaints() {
    return (List<Complaint>) complaintRepository.findAll();
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
}

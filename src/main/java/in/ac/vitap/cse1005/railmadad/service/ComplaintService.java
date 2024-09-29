package in.ac.vitap.cse1005.railmadad.service;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.repository.ComplaintRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

  private final ComplaintRepository complaintRepository;

  @Autowired
  public ComplaintService(ComplaintRepository complaintRepository) {
    this.complaintRepository = complaintRepository;
  }

  public List<Complaint> getComplaints() {
    return (List<Complaint>) complaintRepository.findAll();
  }
}

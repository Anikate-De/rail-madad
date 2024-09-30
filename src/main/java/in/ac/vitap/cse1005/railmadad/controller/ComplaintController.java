package in.ac.vitap.cse1005.railmadad.controller;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ComplaintController {

  private final ComplaintService complaintService;

  @Autowired
  public ComplaintController(ComplaintService complaintService) {
    this.complaintService = complaintService;
  }

  @GetMapping("/complaints")
  public ResponseEntity<Map<String, Object>> getComplaints(HttpServletRequest request) {
    // TODO: Replace with role based complaints retrieval
    log.info(
        "{} with ID: {}",
        request.getAttribute("role").toString(),
        request.getAttribute("id").toString());

    return new ResponseEntity<>(
        Map.of("complaints", complaintService.getComplaints()), HttpStatus.OK);
  }

  @PostMapping("/complaints")
  public ResponseEntity<Map<String, Object>> postComplaints(
      HttpServletRequest request, @RequestBody Complaint complaint) {

    String customerId = request.getAttribute("id").toString();
    UserRole role = UserRole.valueOf(request.getAttribute("role").toString());

    if (role != UserRole.CUSTOMER) {
      return new ResponseEntity<>(
          Map.of("message", "Only customers can post complaints"), HttpStatus.FORBIDDEN);
    }

    try {
      complaint = complaintService.postComplaint(customerId, complaint);
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of("message", "Incomplete Details provided, title is required"),
          HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      log.error("Error while posting complaint: ", e);
      return new ResponseEntity<>(
          Map.of("message", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(Map.of("complaint", complaint), HttpStatus.CREATED);
  }
}

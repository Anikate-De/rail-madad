package in.ac.vitap.cse1005.railmadad.controller;

import in.ac.vitap.cse1005.railmadad.service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    log.info(
        "{} with ID: {}",
        request.getAttribute("role").toString(),
        request.getAttribute("id").toString());

    return new ResponseEntity<>(
        Map.of("complaints", complaintService.getComplaints()), HttpStatus.OK);
  }
}

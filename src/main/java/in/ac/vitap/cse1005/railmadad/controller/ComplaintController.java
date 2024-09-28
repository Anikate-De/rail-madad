package in.ac.vitap.cse1005.railmadad.controller;

import in.ac.vitap.cse1005.railmadad.service.AuthService;
import in.ac.vitap.cse1005.railmadad.service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplaintController {

  private final ComplaintService complaintService;
  private final AuthService authService;

  @Autowired
  public ComplaintController(ComplaintService complaintService, AuthService authService) {
    this.complaintService = complaintService;
    this.authService = authService;
  }

  @GetMapping("/complaints")
  public ResponseEntity<Map<String, Object>> getComplaints(HttpServletRequest request) {
    //    String customerId;
    //    try {
    //      customerId = authService.authenticateCustomer(request.getHeader("Authorization"));
    //    } catch (ExpiredJwtException expiredJwtException) {
    //      return ResponseEntity.status(401).body(Map.of("message", "Token expired."));
    //    } catch (Exception e) {
    //      return ResponseEntity.status(401).body(Map.of("message", "Unauthorized."));
    //    }
    //
    //    System.out.println("Customer " + customerId + " requested for complaints.");

    return ResponseEntity.ok(Map.of("complaints", complaintService.getComplaints()));
  }
}

package in.ac.vitap.cse1005.railmadad.controller;

import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.entity.Message;
import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.exceptions.AccessDeniedException;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling complaint-related operations.
 *
 * <p>This controller provides endpoints for creating, updating, and retrieving complaints.
 */
@Slf4j
@RestController
public class ComplaintController {

  private final ComplaintService complaintService;

  /**
   * Constructs a ComplaintController with the specified ComplaintService and CustomerService.
   *
   * @param complaintService the service for complaint operations
   */
  @Autowired
  public ComplaintController(ComplaintService complaintService) {
    this.complaintService = complaintService;
  }

  /**
   * Endpoint for retrieving complaints based on user role.
   *
   * @param request the HTTP request containing user details
   * @return a ResponseEntity with the list of complaints
   */
  @GetMapping("/complaints")
  public ResponseEntity<List<Complaint>> getComplaints(HttpServletRequest request) {
    String id = request.getAttribute("id").toString();
    UserRole role = UserRole.valueOf(request.getAttribute("role").toString());

    return switch (role) {
      case CUSTOMER -> new ResponseEntity<>(complaintService.getFiledComplaints(id), HttpStatus.OK);
      case OFFICER ->
          new ResponseEntity<>(
              complaintService.getAssignedComplaints(Long.parseLong(id)), HttpStatus.OK);
    };
  }

  /**
   * Endpoint for posting a new complaint.
   *
   * @param request the HTTP request containing user details
   * @param complaint the complaint to be posted
   * @return a ResponseEntity with the created complaint
   */
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

  /**
   * Endpoint for updating the status of a complaint.
   *
   * @param request the HTTP request containing user details
   * @param complaint the complaint with updated status
   * @param complaintId the ID of the complaint to be updated
   * @return a ResponseEntity with the updated complaint
   */
  @PutMapping("/complaints/{complaintId}")
  public ResponseEntity<Map<String, Object>> postComplaints(
      HttpServletRequest request,
      @RequestBody Complaint complaint,
      @PathVariable Long complaintId) {

    String officerId = request.getAttribute("id").toString();
    UserRole role = UserRole.valueOf(request.getAttribute("role").toString());

    if (role != UserRole.OFFICER) {
      return new ResponseEntity<>(
          Map.of("message", "Only officers can update complaint status."), HttpStatus.FORBIDDEN);
    }

    try {
      complaint =
          complaintService.updateComplaintStatus(
              complaintId, complaint.getStatus(), Long.parseLong(officerId));
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of("message", "Incomplete Details provided, Complaint ID and Status are required"),
          HttpStatus.BAD_REQUEST);
    } catch (NoSuchElementException noSuchElementException) {
      return new ResponseEntity<>(Map.of("message", "Complaint not found"), HttpStatus.NOT_FOUND);
    } catch (AccessDeniedException accessDeniedException) {
      return new ResponseEntity<>(
          Map.of("message", "Access Denied, you are not the assigned officer"),
          HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      log.error("Error while updating complaint status: ", e);
      return new ResponseEntity<>(
          Map.of("message", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(Map.of("complaint", complaint), HttpStatus.CREATED);
  }

  /**
   * Endpoint for adding a message to a complaint.
   *
   * @param complaintId the ID of the complaint to which the message is to be added
   * @param message the message to be added
   * @param request the HTTP request containing user details
   * @return a ResponseEntity with the added message
   */
  @PostMapping("/complaints/{complaintId}/messages")
  public ResponseEntity<Object> addMessage(
      @PathVariable Long complaintId, @RequestBody Message message, HttpServletRequest request) {
    String officerId = (String) request.getAttribute("id");
    UserRole role = UserRole.valueOf(request.getAttribute("role").toString());

    if (role == UserRole.CUSTOMER) {
      return new ResponseEntity<>(
          Map.of("message", "Only Officers are allowed to post messages."), HttpStatus.FORBIDDEN);
    }

    try {
      message = complaintService.addMessage(complaintId, message, Long.valueOf(officerId));
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of("message", "Complaint Id and Message body are required."), HttpStatus.BAD_REQUEST);
    } catch (NoSuchElementException noSuchElementException) {
      return new ResponseEntity<>(Map.of("message", "Complaint not found"), HttpStatus.NOT_FOUND);
    } catch (AccessDeniedException accessDeniedException) {
      return new ResponseEntity<>(
          Map.of("message", "Access Denied, you are not the assigned Officer."),
          HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      log.error("Error while updating complaint status: ", e);
      return new ResponseEntity<>(
          Map.of("message", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(message, HttpStatus.CREATED);
  }
}

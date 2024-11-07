package in.ac.vitap.cse1005.railmadad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.UserRole;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.service.OfficerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling officer-related operations.
 *
 * <p>This controller provides endpoints for officer signup and login.
 */
@RestController
public class OfficerController {

  private final OfficerService officerService;
  private final ObjectMapper objectMapper;

  /**
   * Constructs an OfficerController with the specified OfficerService and ObjectMapper.
   *
   * @param officerService the service for officer operations
   * @param objectMapper the object mapper for JSON conversion
   */
  @Autowired
  public OfficerController(OfficerService officerService, ObjectMapper objectMapper) {
    this.officerService = officerService;
    this.objectMapper = objectMapper;
  }

  /**
   * Endpoint for officer signup.
   *
   * @param request the request body containing officer details and password
   * @return a ResponseEntity with a message and the created officer
   */
  @PostMapping(value = "/officers/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, Object> request) {
    Officer officer = objectMapper.convertValue(request, Officer.class);
    String password = (String) request.get("password");

    try {
      officer = officerService.signup(officer, password);
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of("message", "Incomplete details provided. First name and Password are required."),
          HttpStatus.BAD_REQUEST);
    } catch (WeakPasswordException weakPasswordException) {
      return new ResponseEntity<>(
          Map.of(
              "message",
              "Weak password provided. Password must contain at least 8 characters, one"
                  + " uppercase letter, one lowercase letter, one number and one special"
                  + " character. It must not contain any whitespaces."),
          HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(
          Map.of("message", "An error occurred while processing the request."),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(
        Map.of("message", "Officer signup successful", "officer", officer), HttpStatus.CREATED);
  }

  /**
   * Endpoint for officer login.
   *
   * @param request the request body containing officer ID and password
   * @return a ResponseEntity with a message and the authentication token
   */
  @PostMapping(value = "/officers/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> login(
      @RequestBody Map<String, Object> request, HttpServletResponse response) {
    Officer officer = objectMapper.convertValue(request, Officer.class);
    String password = (String) request.get("password");

    String token;
    try {
      token = officerService.login(officer.getId(), password);
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of("message", "Incomplete details provided. ID and Password are required."),
          HttpStatus.BAD_REQUEST);
    } catch (EntityNotFoundException entityNotFoundException) {
      return new ResponseEntity<>(
          Map.of("message", "Officer with the provided ID does not exist."), HttpStatus.NOT_FOUND);
    } catch (PasswordMismatchException passwordMismatchException) {
      return new ResponseEntity<>(
          Map.of("message", "Incorrect password provided."), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return new ResponseEntity<>(
          Map.of("message", "An error occurred while processing the request."),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    Cookie cookie = new Cookie("token", token);
    cookie.setMaxAge(3600);
    cookie.setPath("/");
    response.addCookie(cookie);

    return new ResponseEntity<>(
        Map.of("message", "Officer login successful", "token", token), HttpStatus.ACCEPTED);
  }

  /**
   * Endpoint for retrieving officer details.
   *
   * @param request the HttpServletRequest containing officer ID and role
   * @return a ResponseEntity with the officer details or an error message
   */
  @GetMapping("/getOfficer")
  public ResponseEntity<Map<String, Object>> getOfficer(HttpServletRequest request) {
    UserRole role = UserRole.valueOf(request.getAttribute("role").toString());

    if (role == UserRole.CUSTOMER) {
      return new ResponseEntity<>(
          Map.of("message", "Only officers can request details."), HttpStatus.FORBIDDEN);
    }

    Officer officer;
    try {
      officer = (Officer) request.getAttribute("user");
    } catch (Exception e) {
      return new ResponseEntity<>(
          Map.of("message", "An internal Server Error Occurred."),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(Map.of("officer", officer), HttpStatus.OK);
  }
}

package in.ac.vitap.cse1005.railmadad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.service.OfficerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfficerController {

  private final OfficerService officerService;
  private final ObjectMapper objectMapper;

  @Autowired
  public OfficerController(OfficerService officerService, ObjectMapper objectMapper) {
    this.officerService = officerService;
    this.objectMapper = objectMapper;
  }

  @PostMapping(value = "/officers/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, Object> request) {
    Officer officer = objectMapper.convertValue(request, Officer.class);
    String password = (String) request.get("password");

    try {
      officer = officerService.signup(officer, password);
    } catch (EntityExistsException entityExistsException) {
      return new ResponseEntity<>(
          Map.of("message", "Officer with same ID already exists."), HttpStatus.CONFLICT);
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

  @PostMapping(value = "/officers/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
    long id = ((Integer) request.get("id")).longValue();
    String password = (String) request.get("password");

    String token;
    try {
      token = officerService.login(id, password);
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

    return new ResponseEntity<>(
        Map.of("message", "Officer login successful", "token", token), HttpStatus.ACCEPTED);
  }
}

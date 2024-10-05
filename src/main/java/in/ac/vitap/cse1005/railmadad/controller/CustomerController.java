package in.ac.vitap.cse1005.railmadad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.vitap.cse1005.railmadad.domain.entity.Customer;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.PasswordMismatchException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.service.CustomerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling customer-related operations.
 *
 * <p>This controller provides endpoints for customer signup and login.
 */
@RestController
public class CustomerController {

  private final CustomerService customerService;
  private final ObjectMapper objectMapper;
  private final int cookieExpirySeconds = 3600;

  /**
   * Constructs a CustomerController with the specified CustomerService and ObjectMapper.
   *
   * @param customerService the service for customer operations
   * @param objectMapper the object mapper for JSON conversion
   */
  @Autowired
  public CustomerController(CustomerService customerService, ObjectMapper objectMapper) {
    this.customerService = customerService;
    this.objectMapper = objectMapper;
  }

  /**
   * Endpoint for customer signup.
   *
   * @param request the request body containing customer details and password
   * @return a ResponseEntity with a message and the created customer
   */
  @PostMapping(value = "/customers/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, Object> request) {
    Customer customer = objectMapper.convertValue(request, Customer.class);
    String password = (String) request.get("password");
    try {
      customerService.signup(customer, password);
    } catch (EntityExistsException entityExistsException) {
      return new ResponseEntity<>(
          Map.of("message", "Customer with the same phone number already exists."),
          HttpStatus.CONFLICT);
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of(
              "message",
              "Incomplete details provided. First name, Phone Number and Password are required."),
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
        Map.of("message", "Customer signup successful"), HttpStatus.CREATED);
  }

  /**
   * Endpoint for customer login.
   *
   * @param request the request body containing customer phone number and password
   * @return a ResponseEntity with a message and the authentication token
   */
  @PostMapping(value = "/customers/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> login(
      @RequestBody Map<String, Object> request, HttpServletResponse response) {
    Customer customer = objectMapper.convertValue(request, Customer.class);
    String password = (String) request.get("password");

    String token;
    try {
      token = customerService.login(customer.getPhoneNumber(), password);
    } catch (IncompleteDetailsException incompleteDetailsException) {
      return new ResponseEntity<>(
          Map.of("message", "Incomplete details provided. Phone Number and Password are required."),
          HttpStatus.BAD_REQUEST);
    } catch (EntityNotFoundException entityNotFoundException) {
      return new ResponseEntity<>(
          Map.of("message", "Customer with the provided phone number does not exist."),
          HttpStatus.NOT_FOUND);
    } catch (PasswordMismatchException passwordMismatchException) {
      return new ResponseEntity<>(
          Map.of("message", "Incorrect password provided."), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return new ResponseEntity<>(
          Map.of("message", "An error occurred while processing the request."),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Cookie cookie = new Cookie("auth_token", token);
    cookie.setMaxAge(60);
    cookie.setPath("/");
    response.addCookie(cookie);
    return new ResponseEntity<>(
        Map.of("message", "Customer login successful", "token", token), HttpStatus.ACCEPTED);
  }
}

package in.ac.vitap.cse1005.railmadad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.vitap.cse1005.railmadad.domain.Customer;
import in.ac.vitap.cse1005.railmadad.exceptions.EntityAlreadyExistsException;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.exceptions.WeakPasswordException;
import in.ac.vitap.cse1005.railmadad.service.CustomerService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

  private final CustomerService customerService;
  private final ObjectMapper objectMapper;

  @Autowired
  public CustomerController(CustomerService customerService, ObjectMapper objectMapper) {
    this.customerService = customerService;
    this.objectMapper = objectMapper;
  }

  @PostMapping(value = "/customer_signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, Object> request) {
    Customer customer = objectMapper.convertValue(request, Customer.class);
    String password = (String) request.get("password");

    try {
      customerService.signup(customer, password);
    } catch (EntityAlreadyExistsException entityAlreadyExistsException) {
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
}

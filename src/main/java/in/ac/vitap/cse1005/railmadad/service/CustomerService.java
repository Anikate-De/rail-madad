package in.ac.vitap.cse1005.railmadad.service;

import in.ac.vitap.cse1005.railmadad.domain.Customer;
import in.ac.vitap.cse1005.railmadad.exceptions.EntityAlreadyExistsException;
import in.ac.vitap.cse1005.railmadad.exceptions.IncompleteDetailsException;
import in.ac.vitap.cse1005.railmadad.repository.CustomerRepository;
import in.ac.vitap.cse1005.railmadad.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final PasswordUtils passwordUtils;

  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
    this.passwordUtils = new PasswordUtils();
  }

  public void signup(Customer customer, String password) {
    if (customer.getFirstName() == null || customer.getPhoneNumber() == 0L || password == null) {
      throw new IncompleteDetailsException();
    }

    passwordUtils.checkPasswordStrength(password);
    customer.setPasswordHash(passwordUtils.hashPassword(password));

    try {
      customerRepository.save(customer);
    } catch (DataIntegrityViolationException e) {
      throw new EntityAlreadyExistsException();
    }
  }
}

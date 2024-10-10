package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.ac.vitap.cse1005.railmadad.domain.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a customer in the system.
 *
 * <p>This entity is used to store customer information in the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Customer implements User {

  /** The list of complaints associated with this customer. */
  @JsonIgnore
  @OneToMany(mappedBy = "customer")
  @Builder.Default
  List<Complaint> complaints = List.of();

  /** The unique identifier for the customer. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JsonIgnore
  private String id;

  /** The phone number of the customer. */
  @Column(unique = true)
  private long phoneNumber;

  /** The first name of the customer. */
  @Column(nullable = false)
  private String firstName;

  /** The last name of the customer. */
  private String lastName;

  /** The hashed password of the customer. */
  @JsonIgnore private String passwordHash;

  /** The date the customer registered. */
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateRegistered;

  /** The last login timestamp of the customer. */
  private Instant lastLogin;

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "("
        + "id = "
        + id
        + ", "
        + "phoneNumber = "
        + phoneNumber
        + ", "
        + "firstName = "
        + firstName
        + ", "
        + "lastName = "
        + lastName
        + ", "
        + "passwordHash = "
        + passwordHash
        + ", "
        + "dateRegistered = "
        + dateRegistered
        + ", "
        + "lastLogin = "
        + lastLogin
        + ")";
  }
}

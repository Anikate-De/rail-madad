package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Customer {

  @JsonIgnore
  @OneToMany(mappedBy = "customer")
  @Builder.Default
  private List<Complaint> complaints = List.of();

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JsonIgnore
  private String id;

  @Column(unique = true)
  private long phoneNumber; // The field for the customer's phone number

  @Column(nullable = false)
  private String firstName; // The field for the customer's first name

  private String lastName; // The field for the customer's last name

  @JsonIgnore
  private String passwordHash; // The field for the hashed password

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateRegistered; // The field for the date registered

  private Instant lastLogin; // The field for the last login timestamp

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

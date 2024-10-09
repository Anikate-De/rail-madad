package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.ac.vitap.cse1005.railmadad.domain.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Represents an officer in the system.
 *
 * <p>This entity is used to store officer information in the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Officer implements User {

  /** The list of complaints handled by this officer. */
  @JsonIgnore
  @OneToMany(mappedBy = "officer", fetch = FetchType.EAGER)
  @Builder.Default
  private List<Complaint> complaints = List.of();

  /** The list of messages commented by this officer. */
  @JsonIgnore
  @OneToMany(mappedBy = "officer")
  @Builder.Default
  private List<Message> messages = List.of();

  /** The department to which this officer belongs. */
  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  /** The unique identifier for the officer. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The first name of the officer. */
  @Column(nullable = false)
  private String firstName;

  /** The last name of the officer. */
  private String lastName;

  /** The hashed password of the officer. */
  @JsonIgnore private String passwordHash;

  /** The date the officer registered. */
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateRegistered;

  /** The last login timestamp of the officer. */
  private Instant lastLogin;
}

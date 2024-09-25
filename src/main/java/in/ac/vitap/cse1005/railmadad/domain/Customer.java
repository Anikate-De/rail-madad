package in.ac.vitap.cse1005.railmadad.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
  @Builder.Default
  List<Complaint> complaints = List.of();

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JsonIgnore
  private String id;

  @Column(unique = true)
  private long phoneNumber;

  @Column(nullable = false)
  private String firstName;

  private String lastName;

  @JsonIgnore private String passwordHash;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateRegistered;

  private Instant lastLogin;
}

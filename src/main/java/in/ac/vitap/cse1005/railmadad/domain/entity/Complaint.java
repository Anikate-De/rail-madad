package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Complaint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String title;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ComplaintStatus status = ComplaintStatus.PENDING;

  private String summary;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateFiled;

  @UpdateTimestamp private Instant lastUpdated;

  @ManyToOne()
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne()
  @JoinColumn(name = "officer_id")
  private Officer officer;

  @ManyToOne()
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "complaint")
  private List<Message> messages;

  @OneToMany(mappedBy = "complaint")
  private List<Media> mediaList;
}

package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
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

/**
 * Represents a complaint in the system.
 *
 * <p>This entity is used to store complaint information in the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Complaint {

  /** The unique identifier for the complaint. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The title of the complaint. */
  @Column(nullable = false)
  private String title;

  /** The status of the complaint. */
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ComplaintStatus status = ComplaintStatus.PENDING;

  /** A summary of the complaint. */
  private String summary;

  /** The date the complaint was filed. */
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @JsonSerialize(using = InstantSerializer.class)
  private Instant dateFiled;

  /** The last updated timestamp of the complaint. */
  @UpdateTimestamp
  @JsonSerialize(using = InstantSerializer.class)
  private Instant lastUpdated;

  /** The customer who filed the complaint. */
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  /** The officer handling the complaint. */
  @ManyToOne
  @JoinColumn(name = "officer_id")
  private Officer officer;

  /** The category to which this complaint belongs. */
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  /** The list of messages associated with this complaint. */
  @OneToMany(mappedBy = "complaint")
  @Builder.Default
  private List<Message> messages = List.of();

  /** The list of media associated with this complaint. */
  @OneToMany(mappedBy = "complaint")
  @Builder.Default
  private List<Media> mediaList = List.of();
}

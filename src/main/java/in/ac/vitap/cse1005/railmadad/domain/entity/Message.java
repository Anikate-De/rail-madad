package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a message associated with a complaint in the system.
 *
 * <p>This entity is used to store message information in the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Message {

  /** The unique identifier for the message. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The body of the message. */
  @Column(nullable = false)
  private String body;

  /** The date the message was commented. */
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateCommented;

  /** The complaint to which this message belongs. */
  @ManyToOne
  @JoinColumn(name = "complaint_id")
  @JsonIgnore
  private Complaint complaint;

  /** The officer who commented the message. */
  @ManyToOne
  @JoinColumn(name = "officer_id")
  @JsonIgnore
  private Officer officer;
}

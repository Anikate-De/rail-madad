package in.ac.vitap.cse1005.railmadad.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String body;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant dateCommented;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "complaint_id")
  private Complaint complaint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "officer_id")
  private Officer officer;
}
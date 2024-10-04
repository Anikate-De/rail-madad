package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.ac.vitap.cse1005.railmadad.domain.enums.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents media associated with a complaint in the system.
 *
 * <p>This entity is used to store media information in the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Media {

  /** The unique identifier for the media. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The type of the media. */
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private MediaType mediaType = MediaType.TEXT;

  /** The data of the media. */
  @Column(nullable = false)
  private byte[] data;

  /** The complaint to which this media belongs. */
  @ManyToOne
  @JoinColumn(name = "complaint_id")
  @JsonIgnore
  private Complaint complaint;
}

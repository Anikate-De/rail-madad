package in.ac.vitap.cse1005.railmadad.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Media {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private MediaType mediaType = MediaType.TEXT;

  @Column(nullable = false)
  private byte[] data;

  @ManyToOne()
  @JoinColumn(name = "complaint_id")
  @JsonIgnore
  private Complaint complaint;
}

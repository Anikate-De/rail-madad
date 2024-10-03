package in.ac.vitap.cse1005.railmadad.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a department in the system.
 *
 * <p>This entity is used to store department information in the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Department {

  /** The list of categories managed by this department. */
  @OneToMany(mappedBy = "department")
  @Builder.Default
  @JsonIgnore
  List<Category> managedCategories = List.of();

  /** The list of officers associated with this department. */
  @OneToMany(mappedBy = "department")
  @Builder.Default
  @JsonIgnore
  List<Officer> officers = List.of();

  /** The unique identifier for the department. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The name of the department. */
  @Column(nullable = false)
  private String name;
}

package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Category;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Category entity operations.
 *
 * <p>This interface provides methods for performing CRUD operations on Category entities.
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

  /**
   * Finds a Category by its name, ignoring case.
   *
   * @param name the name of the category to find
   * @return an Optional containing the found Category, or an empty Optional if no Category is found
   */
  Optional<Category> findByNameIgnoreCase(@NonNull String name);
}

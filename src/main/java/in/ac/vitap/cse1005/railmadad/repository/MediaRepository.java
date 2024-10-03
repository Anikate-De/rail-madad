package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Media;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository interface for handling CRUD operations on Media entities. */
@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {}

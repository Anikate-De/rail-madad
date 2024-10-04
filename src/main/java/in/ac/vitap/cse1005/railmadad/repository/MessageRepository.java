package in.ac.vitap.cse1005.railmadad.repository;

import in.ac.vitap.cse1005.railmadad.domain.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository interface for handling CRUD operations on Message entities. */
@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {}

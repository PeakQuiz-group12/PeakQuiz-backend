package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByUsername(String username);
}

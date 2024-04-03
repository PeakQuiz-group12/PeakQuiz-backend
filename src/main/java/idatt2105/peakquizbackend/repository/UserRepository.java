package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByUsername(String username);
}

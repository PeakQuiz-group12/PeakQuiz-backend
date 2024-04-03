package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
  User findUserByUsername(String username);
}

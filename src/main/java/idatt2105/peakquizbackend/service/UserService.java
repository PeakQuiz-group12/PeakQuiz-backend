package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User addUser(User user) {
    return userRepository.save(user);
  }

  public Optional<User> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findUserByUserId(Long id) {
    return userRepository.findById(id);
  }

}

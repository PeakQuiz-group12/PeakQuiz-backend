package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.repository.UserRepository;
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

  public UserRepository getUserRepository() {
    return userRepository;
  }
}

package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.UserNotFoundException;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);
  }

  public boolean usernameExists(String username) {
    Optional<User> existingUser = userRepository.findByUsername(username);
      return existingUser.isPresent();
  }

  public User findUserByUserId(Long id) {
    return userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
  }

}

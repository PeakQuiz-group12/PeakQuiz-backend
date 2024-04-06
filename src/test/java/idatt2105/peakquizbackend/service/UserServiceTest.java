package idatt2105.peakquizbackend.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.fail;

import idatt2105.peakquizbackend.exceptions.UserAlreadyExistsException;
import idatt2105.peakquizbackend.exceptions.UserNotFoundException;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTest {
  @TestConfiguration
  static class UserServiceTestConfiguration {
    @Bean
    public UserService userService(UserRepository userRepository) {
      return new UserService(userRepository);
    }
  }

  @Autowired
  UserService userService;

  @MockBean
  private UserRepository userRepository;

  User existingUser;

  User nonExistentUser;
  @Before
  public void setup() {
    existingUser = new User("name", "example@mail.com", "password");

    when(userRepository.findUserByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));
    when(userRepository.existsById(existingUser.getId())).thenReturn(true);
    when(userRepository.findUserByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));
    when(userRepository.save(existingUser)).thenReturn(existingUser);
    doNothing().when(userRepository).delete(existingUser);
    when(userRepository.findAll()).thenReturn(List.of(existingUser));

    nonExistentUser = new User("novel", "novel@novel.com", "novel");

    when(userRepository.findUserByUsername(nonExistentUser.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findById(nonExistentUser.getId())).thenReturn(Optional.empty());
    when(userRepository.save(nonExistentUser)).thenReturn(nonExistentUser);
    doNothing().when(userRepository).delete(nonExistentUser);
  }

  @Test
  public void testSaveUserExistingUser() {
    assertDoesNotThrow(()-> {
      userService.saveUser(existingUser);
    });
  }

  @Test
  public void testUsernameExistsBadUsername() {
    boolean result = userService.usernameExists(existingUser.getUsername());
    assertTrue(result);
  }

  @Test
  public void testFindByUserIdBadUserId() {
    assertThrows(UserNotFoundException.class, () -> {
      userService.findUserByUserId(nonExistentUser.getId());
      fail();
    });
  }

  @Test
  public void testFindUserByUsernameBadUsername() {
    assertThrows(UserNotFoundException.class, () -> {
      userService.findUserByUsername(nonExistentUser.getUsername());
    });
  }
  @Test
  public void testSaveUser() {
    User newUser;

    try {
      newUser = userService.saveUser(nonExistentUser);
    } catch (RuntimeException e) {
      e.printStackTrace();
      fail();
      return;
    }
    assertEquals(nonExistentUser.getId(), newUser.getId());
    assertEquals(nonExistentUser.getUsername(), newUser.getUsername());
    assertEquals(nonExistentUser.getEmail(), newUser.getEmail());
    assertEquals(nonExistentUser.getPassword(), newUser.getPassword());
    assertEquals(nonExistentUser.getCreatedOn(), newUser.getCreatedOn());
    assertEquals(nonExistentUser.getTags(), newUser.getTags());
    assertEquals(nonExistentUser.getCollaborations(), newUser.getCollaborations());
  }
}

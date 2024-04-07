package idatt2105.peakquizbackend.repository;

import static org.junit.jupiter.api.Assertions.*;

import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserEntityMapping() {
        // Given
        User user = new User("testUser", "test@example.com", "password");

        // When
        entityManager.persistAndFlush(user);
        User persistedUser = entityManager.find(User.class, user.getId());

        // Then
        assertNotNull(persistedUser.getId());
        assertEquals(user.getUsername(), persistedUser.getUsername());
        assertEquals(user.getEmail(), persistedUser.getEmail());
        assertEquals(user.getPassword(), persistedUser.getPassword());
        assertNotNull(persistedUser.getCreatedOn());
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User("testUser", "test@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();
        User detected = userRepository.findUserByUsername(user.getUsername()).get();

        assertEquals(user.getId(), detected.getId());
    }

    @Test
    public void testUserEntityValidation() {
        // Given
        User invalidUser = new User("a", "invalid_email", "pass");

        // When & Then
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(invalidUser));
    }

    @Test
    public void testUserGameRelationship() {
        // Given
        User user = new User("testUser", "test@example.com", "password");
        Game game = new Game(1, (byte) 1, "", user, new Quiz());
        user.getGames().add(game);

        // When
        entityManager.persistAndFlush(user);
        User persistedUser = entityManager.find(User.class, user.getId());

        // Then
        assertNotNull(persistedUser.getGames());
        assertFalse(persistedUser.getGames().isEmpty());
        assertTrue(persistedUser.getGames().contains(game));
    }

}

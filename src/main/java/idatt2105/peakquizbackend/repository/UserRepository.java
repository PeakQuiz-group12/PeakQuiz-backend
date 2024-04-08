package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository interface for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their username.
     * 
     * @param username
     *            The username to search for
     * @return An Optional containing the user if found, or an empty Optional otherwise
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Find a user by their email address.
     * 
     * @param email
     *            The email address to search for
     * @return The user object if found, or null if not found
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Update the password of a user specified by their email address.
     * 
     * @param email
     *            The email address of the user
     * @param password
     *            The new password to set
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updateUserPassword(String email, String password);
}

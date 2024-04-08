package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.UserNotFoundException;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Saves a user.
     * 
     * @param user
     *            The user to save
     * @return The saved user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by username.
     * 
     * @param username
     *            The username of the user to find
     * @return The found user
     * @throws UserNotFoundException
     *             if the user is not found
     */
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Checks if a username exists.
     * 
     * @param username
     *            The username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    /**
     * Checks if an email exists.
     *
     * @param email
     *            The email to check
     * @return true if the email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }


    /**
     * Finds a user by user ID.
     * 
     * @param id
     *            The ID of the user to find
     * @return The found user
     * @throws UserNotFoundException
     *             if the user is not found
     */
    public User findUserByUserId(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Retrieves all users.
     * 
     * @return List of all users
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}

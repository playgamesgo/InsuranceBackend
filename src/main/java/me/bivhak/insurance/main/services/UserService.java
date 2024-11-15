package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.User;
import me.bivhak.insurance.main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return an Optional containing the User if found, or empty if not.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user.
     * @return an Optional containing the User if found, or empty if not.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user.
     * @return an Optional containing the User if found, or empty if not.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
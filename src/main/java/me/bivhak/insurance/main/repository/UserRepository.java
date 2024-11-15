package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User entity by its username.
     *
     * @param username the username of the User to find.
     * @return an Optional containing the found User, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a User entity by its email.
     *
     * @param email the email of the User to find.
     * @return an Optional containing the found User, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a User entity with the given username exists.
     *
     * @param username the username of the User to check.
     * @return true if a User with the given username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a User entity with the given email exists.
     *
     * @param email the email of the User to check.
     * @return true if a User with the given email exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}
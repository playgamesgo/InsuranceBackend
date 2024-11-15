package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.Role;
import me.bivhak.insurance.main.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role entity by its name.
     *
     * @param name the name of the Role to find.
     * @return an Optional containing the found Role, or empty if not found.
     */
    Optional<Role> findByName(Roles name);

    /**
     * Checks if a Role entity with the given name exists.
     *
     * @param name the name of the Role to check.
     * @return true if a Role with the given name exists, false otherwise.
     */
    Boolean existsByName(Roles name);
}
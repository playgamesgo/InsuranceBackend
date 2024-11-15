package me.bivhak.insurance.main.repository;

import java.util.Optional;

import me.bivhak.insurance.main.models.ERole;
import me.bivhak.insurance.main.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);

    boolean existsByName(ERole roleEnum);
}

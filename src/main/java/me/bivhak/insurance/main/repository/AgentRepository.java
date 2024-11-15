package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
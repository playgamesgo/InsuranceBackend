package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
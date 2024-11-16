package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

}

package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Insurance;
import me.bivhak.insurance.main.models.InsuranceAgentPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceAgentRepository extends JpaRepository<InsuranceAgentPermission, Long> {
    boolean existsByInsuranceAndAgent(Insurance insurance, Agent agent);

    List<InsuranceAgentPermission> findByInsuranceAndAgent(Insurance insurance, Agent agent);
}

package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Insurance;
import me.bivhak.insurance.main.models.InsuranceAgentPermission;
import me.bivhak.insurance.main.repository.InsuranceAgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceAgentService {
    private final InsuranceAgentRepository insuranceAgentRepository;

    public InsuranceAgentService(InsuranceAgentRepository insuranceAgentRepository) {
        this.insuranceAgentRepository = insuranceAgentRepository;
    }

    public void save(InsuranceAgentPermission permission) {
        insuranceAgentRepository.save(permission);
    }

    public boolean isAgentPermissionExists(Insurance insurance, Agent agent) {
        return insuranceAgentRepository.existsByInsuranceAndAgent(insurance, agent);
    }

    public List<InsuranceAgentPermission> findByInsuranceAndAgent(Insurance insurance, Agent agent) {
        return insuranceAgentRepository.findByInsuranceAndAgent(insurance, agent);
    }
}

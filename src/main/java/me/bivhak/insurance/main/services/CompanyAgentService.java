package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.CompanyAgentPermission;
import me.bivhak.insurance.main.repository.CompanyAgentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyAgentService {
    private final CompanyAgentRepository companyAgentRepository;

    public CompanyAgentService(CompanyAgentRepository companyAgentRepository) {
        this.companyAgentRepository = companyAgentRepository;
    }

    public void save(CompanyAgentPermission permission) {
        companyAgentRepository.save(permission);
    }

    public void deleteByCompanyIdAndAgentId(Long companyId, Long agentId) {
        companyAgentRepository.deleteByCompanyIdAndAgentId(companyId, agentId);
    }

    public Optional<CompanyAgentPermission> findByCompanyIdAndAgentId(Long companyId, Long agentId) {
        return companyAgentRepository.findByCompanyIdAndAgentId(companyId, agentId);
    }
}

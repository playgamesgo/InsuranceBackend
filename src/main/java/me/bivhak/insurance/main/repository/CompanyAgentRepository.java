package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.CompanyAgentPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyAgentRepository extends JpaRepository<CompanyAgentPermission, Long> {
    Optional<CompanyAgentPermission> findByCompanyIdAndAgentId(Long companyId, Long agentId);
    List<CompanyAgentPermission> findByCompanyId(Long companyId);
    List<CompanyAgentPermission> findByAgentId(Long agentId);
    void deleteByCompanyIdAndAgentId(Long companyId, Long agentId);
}

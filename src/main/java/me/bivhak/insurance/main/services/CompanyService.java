package me.bivhak.insurance.main.services;

import lombok.AllArgsConstructor;
import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Company;
import me.bivhak.insurance.main.repository.AgentRepository;
import me.bivhak.insurance.main.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService implements UserDetailsService {

    CompanyRepository companyRepository;
    AgentRepository agentRepository;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Company Not Found with username: " + username));

        return UserDetailsImpl.build(company);
    }

    public boolean existsByUsername(String username) {
        return companyRepository.existsByUsername(username);
    }

    public ResponseEntity<Boolean> pinUser(String login , String nameOfCompany) {
        Agent agent = agentRepository.findByUsername(login)
                .orElseThrow(() ->  new UsernameNotFoundException("Agent Not Found with username: " + login));
        Company company = companyRepository.findByUsername(nameOfCompany)
                .orElseThrow(() -> new UsernameNotFoundException("Company Not Found with username: " + nameOfCompany));

        return ResponseEntity.ok(company.getAgents().add(agent));
    }

    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }

    public void save(Company company) {
        companyRepository.save(company);
    }

    public Optional<Company> findById(Long userId) {
        return companyRepository.findById(userId);
    }
}
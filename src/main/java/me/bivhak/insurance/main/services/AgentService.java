package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AgentService implements UserDetailsService {
    @Autowired
    AgentRepository agentRepository;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agent agent = agentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Agent Not Found with username: " + username));

        return UserDetailsImpl.build(agent);
    }

    public boolean existsByUsername(String username) {
        return agentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return agentRepository.existsByEmail(email);
    }

    public void save(Agent agent) {
        agentRepository.save(agent);
    }

    public Optional<Agent> findById(Long userId) {
        return agentRepository.findById(userId);
    }

    public List<Agent> findAllById(Set<Long> agentIds) {
        return agentRepository.findAllById(agentIds);
    }
}
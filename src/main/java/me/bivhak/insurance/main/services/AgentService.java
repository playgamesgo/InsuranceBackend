package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Agent save(Agent agent) {
        return agentRepository.save(agent);
    }

    public Optional<Agent> findById(Long userId) {
        return agentRepository.findById(userId);
    }
}
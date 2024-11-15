package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.Company;
import me.bivhak.insurance.main.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService implements UserDetailsService {
    @Autowired
    CompanyRepository companyRepository;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Company Not Found with username: " + username));

        return UserDetailsImpl.build(company);
    }

    public boolean existsByUsername(String username) {
        return companyRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }

    public void save(Company company) {
        companyRepository.save(company);
    }
}
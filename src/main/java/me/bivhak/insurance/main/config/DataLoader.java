package me.bivhak.insurance.main.config;

import me.bivhak.insurance.main.models.ERole;
import me.bivhak.insurance.main.models.Role;
import me.bivhak.insurance.main.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        for (ERole roleEnum : ERole.values()) {
            if (!roleRepository.existsByName(roleEnum)) {
                Role role = new Role(roleEnum);
                roleRepository.save(role);
            }
        }
    }
}
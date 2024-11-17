package me.bivhak.insurance.main.config;

import me.bivhak.insurance.main.models.ERole;
import me.bivhak.insurance.main.models.Role;
import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.services.DictionaryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final DictionaryService dictionaryService;

    public DataLoader(RoleRepository roleRepository, DictionaryService dictionaryService) {
        this.roleRepository = roleRepository;
        this.dictionaryService = dictionaryService;
    }

    @Override
    public void run(String... args) {
        for (ERole roleEnum : ERole.values()) {
            if (!roleRepository.existsByName(roleEnum)) {
                Role role = new Role(roleEnum);
                roleRepository.save(role);
            }
        }

        dictionaryService.saveDictionary("health", List.of("Заболевания и травмы", "Несчастные случаи", "Экстренная медицинская помощь", "Реабилитация"));
        dictionaryService.saveDictionary("travel", List.of("Медицинские риски", "Отмена или прерывание поездки", "Страхование от утраты багажа", "Страхование от отмены поездки"));
    }
}
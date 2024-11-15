package me.bivhak.insurance.main.controllers;

import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.security.jwt.JwtUtils;
import me.bivhak.insurance.main.services.CompanyService;
import me.bivhak.insurance.main.services.RefreshTokenService;
import me.bivhak.insurance.main.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
public class CompanyController extends AbstractUserController {
    public CompanyController(PasswordEncoder encoder, RoleRepository roleRepository,
                             @Qualifier("companyAuthenticationProvider") DaoAuthenticationProvider companyAuthenticationProvider,
                             JwtUtils jwtUtils, RefreshTokenService refreshTokenService, CompanyService companyService) {
        super("company", encoder, roleRepository, companyAuthenticationProvider, jwtUtils, refreshTokenService, null, null, companyService);
    }
}

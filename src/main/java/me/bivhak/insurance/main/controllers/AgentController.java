package me.bivhak.insurance.main.controllers;

import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.security.jwt.JwtUtils;
import me.bivhak.insurance.main.services.AgentService;
import me.bivhak.insurance.main.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/agent")
public class AgentController extends AbstractUserController {

    public AgentController(PasswordEncoder encoder, RoleRepository roleRepository,
                           @Qualifier("agentAuthenticationProvider") DaoAuthenticationProvider agentAuthenticationProvider,
                           JwtUtils jwtUtils, RefreshTokenService refreshTokenService, AgentService agentService) {
        super("agent", encoder, roleRepository, agentAuthenticationProvider, jwtUtils, refreshTokenService, null, agentService, null);
    }
}

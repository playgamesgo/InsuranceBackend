package me.bivhak.insurance.main.controllers;


import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.security.jwt.JwtUtils;
import me.bivhak.insurance.main.services.RefreshTokenService;
import me.bivhak.insurance.main.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController extends AbstractUserController {
    public UserController(PasswordEncoder encoder, RoleRepository roleRepository,
                          @Qualifier("userAuthenticationProvider") DaoAuthenticationProvider userAuthenticationProvider,
                          JwtUtils jwtUtils, RefreshTokenService refreshTokenService, UserService userService) {
        super("user", encoder, roleRepository, userAuthenticationProvider, jwtUtils, refreshTokenService, userService, null, null);
    }
}

package me.bivhak.insurance.main.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.bivhak.insurance.main.exception.TokenRefreshException;
import me.bivhak.insurance.main.models.*;
import me.bivhak.insurance.main.payload.request.LoginRequest;
import me.bivhak.insurance.main.payload.request.SignupRequest;
import me.bivhak.insurance.main.payload.request.TokenRefreshRequest;
import me.bivhak.insurance.main.payload.response.JwtResponse;
import me.bivhak.insurance.main.payload.response.MessageResponse;
import me.bivhak.insurance.main.payload.response.TokenRefreshResponse;
import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.security.jwt.JwtUtils;
import me.bivhak.insurance.main.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

public abstract class AbstractUserController {
    private final String type;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final DaoAuthenticationProvider userAuthenticationProvider;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final AgentService agentService;
    private final CompanyService companyService;

    public AbstractUserController(String type, PasswordEncoder encoder, RoleRepository roleRepository,
                                  DaoAuthenticationProvider userAuthenticationProvider, JwtUtils jwtUtils,
                                  RefreshTokenService refreshTokenService, UserService userService, AgentService agentService,
                                  CompanyService companyService) {
        this.type = type;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.agentService = agentService;
        this.companyService = companyService;
    }

    @PostMapping("/signup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Username is already taken!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Email is already in use!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        switch (type) {
            case "user" -> {
                if (userService.existsByUsername(signUpRequest.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                }

                if (userService.existsByEmail(signUpRequest.getEmail())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
                }

                User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                        encoder.encode(signUpRequest.getPassword()));
                user.setRole(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

                userService.save(user);

                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            }
            case "agent" -> {
                if (agentService.existsByUsername(signUpRequest.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                }

                if (agentService.existsByEmail(signUpRequest.getEmail())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
                }

                Agent agent = new Agent(signUpRequest.getUsername(), signUpRequest.getEmail(),
                        encoder.encode(signUpRequest.getPassword()));
                agent.setRole(roleRepository.findByName(ERole.ROLE_AGENT).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

                agentService.save(agent);

                return ResponseEntity.ok(new MessageResponse("Agent registered successfully!"));
            }

            case "company" -> {
                if (companyService.existsByUsername(signUpRequest.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                }

                if (companyService.existsByEmail(signUpRequest.getEmail())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
                }

                Company company = new Company(signUpRequest.getUsername(), signUpRequest.getEmail(),
                        encoder.encode(signUpRequest.getPassword()));
                company.setRole(roleRepository.findByName(ERole.ROLE_COMPANY).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

                companyService.save(company);

                return ResponseEntity.ok(new MessageResponse("Company registered successfully!"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid user type!"));
    }

    @PostMapping("/signin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Invalid username or password!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest signUpRequest) {
        Authentication authentication = userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(type, userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), userDetails.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/refreshtoken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Invalid refresh token!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
        switch (type) {
            case "user" -> {
                return refreshTokenService.findByToken(refreshRequest.getRefreshToken())
                        .map(refreshTokenService::verifyExpiration)
                        .map(RefreshToken::getUser)
                        .map(userDetails -> {
                            String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
                            return ResponseEntity.ok(new TokenRefreshResponse(jwt, refreshRequest.getRefreshToken()));
                        })
                        .orElseThrow(() -> new TokenRefreshException(refreshRequest.getRefreshToken(), "Refresh token is not in database!"));
            }
            case "agent" -> {
                return refreshTokenService.findByToken(refreshRequest.getRefreshToken())
                        .map(refreshTokenService::verifyExpiration)
                        .map(RefreshToken::getAgent)
                        .map(agentDetails -> {
                            String jwt = jwtUtils.generateTokenFromUsername(agentDetails.getUsername());
                            return ResponseEntity.ok(new TokenRefreshResponse(jwt, refreshRequest.getRefreshToken()));
                        })
                        .orElseThrow(() -> new TokenRefreshException(refreshRequest.getRefreshToken(), "Refresh token is not in database!"));
            }
            case "company" -> {
                return refreshTokenService.findByToken(refreshRequest.getRefreshToken())
                        .map(refreshTokenService::verifyExpiration)
                        .map(RefreshToken::getCompany)
                        .map(companyDetails -> {
                            String jwt = jwtUtils.generateTokenFromUsername(companyDetails.getUsername());
                            return ResponseEntity.ok(new TokenRefreshResponse(jwt, refreshRequest.getRefreshToken()));
                        })
                        .orElseThrow(() -> new TokenRefreshException(refreshRequest.getRefreshToken(), "Refresh token is not in database!"));
            }
        }
        return null;
    }

    @PostMapping("/signout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> signOut() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(type, userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
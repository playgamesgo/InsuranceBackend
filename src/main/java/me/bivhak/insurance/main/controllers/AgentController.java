package me.bivhak.insurance.main.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.payload.response.AgentSelfResponse;
import me.bivhak.insurance.main.payload.response.MessageResponse;
import me.bivhak.insurance.main.payload.response.ShortCompanyResponse;
import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.security.jwt.JwtUtils;
import me.bivhak.insurance.main.services.AgentService;
import me.bivhak.insurance.main.services.RefreshTokenService;
import me.bivhak.insurance.main.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/agent")
public class AgentController extends AbstractUserController {

    private final AgentService agentService;

    public AgentController(PasswordEncoder encoder, RoleRepository roleRepository,
                           @Qualifier("agentAuthenticationProvider") DaoAuthenticationProvider agentAuthenticationProvider,
                           JwtUtils jwtUtils, RefreshTokenService refreshTokenService, AgentService agentService) {
        super("agent", encoder, roleRepository, agentAuthenticationProvider, jwtUtils, refreshTokenService, null, agentService, null);
        this.agentService = agentService;
    }

    @GetMapping("/self")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent details retrieved successfully!", content = @Content(schema = @Schema(implementation = AgentSelfResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not an agent!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Agent not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getSelf() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not an agent!"));
        }

        Optional<Agent> agent = agentService.findById(userDetails.getId());

        if (agent.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent not found!"));
        }

        return ResponseEntity.ok(new AgentSelfResponse(agent.get().getId(), agent.get().getUsername(), agent.get().getEmail(),
                agent.get().getCompanies().stream().map(c -> new ShortCompanyResponse(c.getId(), c.getUsername(), c.getEmail())).collect(Collectors.toSet())));
    }
}

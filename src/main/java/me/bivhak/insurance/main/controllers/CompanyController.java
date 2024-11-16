package me.bivhak.insurance.main.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Company;
import me.bivhak.insurance.main.payload.request.AssignAgentRequest;
import me.bivhak.insurance.main.payload.response.*;
import me.bivhak.insurance.main.repository.AgentRepository;
import me.bivhak.insurance.main.repository.RoleRepository;
import me.bivhak.insurance.main.security.jwt.JwtUtils;
import me.bivhak.insurance.main.services.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/company")
public class CompanyController extends AbstractUserController {
    private final CompanyService companyService;
    private final AgentService agentService;
    private final AgentRepository agentRepository;
    private final InsuranceService insuranceService;

    public CompanyController(PasswordEncoder encoder, RoleRepository roleRepository,
                             @Qualifier("companyAuthenticationProvider") DaoAuthenticationProvider companyAuthenticationProvider,
                             JwtUtils jwtUtils, RefreshTokenService refreshTokenService, CompanyService companyService,
                             AgentService agentService, AgentRepository agentRepository, InsuranceService insuranceService) {
        super("company", encoder, roleRepository, companyAuthenticationProvider, jwtUtils, refreshTokenService, null, null, companyService);
        this.companyService = companyService;
        this.agentService = agentService;
        this.agentRepository = agentRepository;
        this.insuranceService = insuranceService;
    }

    @PostMapping("/assignagent")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent assigned successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not a company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Agent not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> assignAgent(@Valid @RequestBody AssignAgentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not a company!"));
        }

        Company company = companyService.findById(userDetails.getId()).orElse(null);

        if (company == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        Agent agent;
        if (request.getAgentId() != null) {
            agent = agentService.findById(request.getAgentId()).orElse(null);
        } else if (request.getAgentName() != null) {
            agent = agentRepository.findByUsername(request.getAgentName()).orElse(null);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent not found!"));
        }

        if (agent == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent not found!"));
        }

        if (company.getAgents().stream().anyMatch(a -> a.getId().equals(agent.getId()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent already assigned to company!"));
        }


        company.getAgents().add(agent);

        companyService.save(company);

        return ResponseEntity.ok(new MessageResponse("Agent assigned successfully!"));
    }

    @DeleteMapping("/unassignagent")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent unassigned successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not a company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Agent not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> unassignAgent(Long agentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not a company!"));
        }

        Company company = companyService.findById(userDetails.getId()).orElse(null);

        if (company == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        Agent agent = agentService.findById(agentId).orElse(null);


        if (agent == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent not found!"));
        }

        company.getAgents().remove(agent);
        companyService.save(company);

        return ResponseEntity.ok(new MessageResponse("Agent unassigned successfully!"));
    }

    @GetMapping("/getagents")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agents retrieved successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not a company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getAgents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not a company!"));
        }

        Company company = companyService.findById(userDetails.getId()).orElse(null);

        if (company == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        List<AgentResponse> agents = new ArrayList<>();
        company.getAgents().forEach(agent ->
                agents.add(new AgentResponse(agent.getId(), agent.getUsername(), agent.getEmail())));

        return ResponseEntity.ok(agents);
    }


    @PostMapping("/pinuser")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent pinned to company successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not a company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Agent not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Agent already assigned to company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> pinAgent(@RequestParam String agentLogin, @RequestParam String companyUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not a company!"));
        }

        Company company = companyService.findById(userDetails.getId()).orElse(null);
        if (company == null || !company.getUsername().equals(companyUsername)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found or access denied!"));
        }

        Agent agent = agentRepository.findByUsername(agentLogin).orElse(null);
        if (agent == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent not found!"));
        }

        if (company.getAgents().stream().anyMatch(a -> a.getId().equals(agent.getId()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent already assigned to company!"));
        }

        company.getAgents().add(agent);
        companyService.save(company);

        return ResponseEntity.ok(new MessageResponse("Agent pinned to company successfully!"));
    }

    @GetMapping("/self")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company retrieved successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not a company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getSelf() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not a company!"));
        }

        Company company = companyService.findById(userDetails.getId()).orElse(null);

        if (company == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        return ResponseEntity.ok(new CompanyResponse(company.getId(), company.getUsername(), company.getEmail(),
                company.getAgents().stream().map(agent ->
                        new AgentResponse(agent.getId(), agent.getUsername(), agent.getEmail())).collect(Collectors.toSet())));
    }

    @GetMapping("/getassignedinsurances")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assigned insurances retrieved successfully!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: User is not a company!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Agent not found!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getAssignedInsurances(@Valid Long agentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not a company!"));
        }

        Company company = companyService.findById(userDetails.getId()).orElse(null);

        if (company == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        Agent agent = agentService.findById(agentId).orElse(null);

        if (agent == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent not found!"));
        }

        return ResponseEntity.ok(insuranceService.findAll().stream()
                .filter(insurance -> insurance.getAgents().stream().anyMatch(a -> a.getId().equals(agentId)))
                .map(insurance -> new InsuranceResponse(insurance.getId(), company.getId(), insurance.getName(), insurance.getDescription(),
                        insurance.getObjectInsurance(), insurance.getRiskInsurance(), insurance.getConditionsInsurance(),
                        insurance.getMaxAmount(), insurance.getAmount(), insurance.getExpiresIn(), insurance.getDuration(), insurance.getAgents())));
    }
}

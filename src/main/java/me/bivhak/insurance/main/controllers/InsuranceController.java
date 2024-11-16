package me.bivhak.insurance.main.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import me.bivhak.insurance.main.models.*;
import me.bivhak.insurance.main.payload.request.CreateInsuranceRequest;
import me.bivhak.insurance.main.payload.request.UpdateInsuranceRequest;
import me.bivhak.insurance.main.payload.response.InsuranceResponse;
import me.bivhak.insurance.main.payload.response.MessageResponse;
import me.bivhak.insurance.main.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/insurance")
public class InsuranceController {
    private final CompanyService companyService;
    private final InsuranceService insuranceService;
    private final AgentService agentService;
    private final InsuranceAgentService insuranceAgentService;

    public InsuranceController(CompanyService companyService, InsuranceService insuranceService, AgentService agentService, InsuranceAgentService insuranceAgentService) {
        this.companyService = companyService;
        this.insuranceService = insuranceService;
        this.agentService = agentService;
        this.insuranceAgentService = insuranceAgentService;
    }

    @PostMapping("/create")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance created successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to create insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: All fields are required!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> createInsurance(@Valid @RequestBody CreateInsuranceRequest createInsuranceRequest) {
        // Check if user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        // Check if user has permission to create insurance
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to create insurance!"));
        }

        // Check if company exists
        if (companyService.findById(createInsuranceRequest.getCompanyId()).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        Company company = companyService.findById(createInsuranceRequest.getCompanyId()).get();

        // Check if user is a company and creates insurance for self
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            if (!createInsuranceRequest.getCompanyId().equals(userDetails.getId())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to create this insurance!"));
            }
        }

        // Check if user is an agent and has permission to create insurance for this company
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                agentService.findById(userDetails.getId()).isPresent() &&
                !company.getAgents().contains(agentService.findById(userDetails.getId()).get())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to create insurance!"));
        }

        Insurance insurance = new Insurance(company,
                createInsuranceRequest.getName(), createInsuranceRequest.getDescription(), createInsuranceRequest.getObjectInsurance(),
                createInsuranceRequest.getRiskInsurance(), createInsuranceRequest.getConditionsInsurance(),
                createInsuranceRequest.getMaxAmount(), createInsuranceRequest.getAmount(), createInsuranceRequest.getExpiresIn(),
                createInsuranceRequest.getDuration()
        );

        insuranceService.save(insurance);

        return ResponseEntity.ok(new InsuranceResponse(insurance.getId(), insurance.getCompany().getId(), insurance.getName(), insurance.getDescription(),
                insurance.getObjectInsurance(), insurance.getRiskInsurance(), insurance.getConditionsInsurance(),
                insurance.getMaxAmount(), insurance.getAmount(), insurance.getExpiresIn(), insurance.getDuration(), insurance.getAgents()));
    }

    @GetMapping("/get")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance retrieved successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to get insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Insurance not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getInsurance(Long insuranceId) {
        // Check if user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        // Check if user has permission to get insurance
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to get insurance!"));
        }

        // Check if insurance exists
        if (insuranceService.findById(insuranceId).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Insurance not found!"));
        }

        Insurance insurance = insuranceService.findById(insuranceId).get();

        // Check is user is an agent and has permission to get insurance
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                agentService.findById(userDetails.getId()).isPresent() &&
                insurance.getAgents().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to get insurance!"));
        }

        return ResponseEntity.ok(new InsuranceResponse(insurance.getId(), insurance.getCompany().getId(), insurance.getName(), insurance.getDescription(),
                insurance.getObjectInsurance(), insurance.getRiskInsurance(), insurance.getConditionsInsurance(),
                insurance.getMaxAmount(), insurance.getAmount(), insurance.getExpiresIn(), insurance.getDuration(), insurance.getAgents()));
    }

    @PutMapping("/update")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance updated successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to update insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Insurance not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> updateInsurance(@Valid @RequestBody UpdateInsuranceRequest updateInsuranceRequest, @Valid Long insuranceId) {
        // Check if user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        // Check if user has permission to update insurance
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to update insurance!"));
        }

        // Check if company exists
        if (updateInsuranceRequest.getCompanyId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        // Check if insurance exists
        if (insuranceService.findById(insuranceId).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Insurance not found!"));
        }

        Insurance insurance = insuranceService.findById(insuranceId).get();

        // Check if user is an agent and has permission to update insurance
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT"))) {
            if (agentService.findById(userDetails.getId()).isPresent() &&
                    insurance.getAgents().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()))) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to update insurance!"));
            }
            // Check if user is a company and updates insurance for self
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            if (!insurance.getCompany().getId().equals(userDetails.getId())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to update insurance!"));
            }
        }

        // Check if user is an agent and has permission to update insurance for this company
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                agentService.findById(userDetails.getId()).isPresent() &&
                !insurance.getCompany().getAgents().contains(agentService.findById(userDetails.getId()).get())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to update insurance!"));
        }

        if (updateInsuranceRequest.getName() != null) insurance.setName(updateInsuranceRequest.getName());
        if (updateInsuranceRequest.getDescription() != null) insurance.setDescription(updateInsuranceRequest.getDescription());
        if (updateInsuranceRequest.getObjectInsurance() != null) insurance.setObjectInsurance(updateInsuranceRequest.getObjectInsurance());
        if (updateInsuranceRequest.getRiskInsurance() != null) insurance.setRiskInsurance(updateInsuranceRequest.getRiskInsurance());
        if (updateInsuranceRequest.getConditionsInsurance() != null) insurance.setConditionsInsurance(updateInsuranceRequest.getConditionsInsurance());
        if (updateInsuranceRequest.getMaxAmount() != null) insurance.setMaxAmount(updateInsuranceRequest.getMaxAmount());
        if (updateInsuranceRequest.getAmount() != null) insurance.setAmount(updateInsuranceRequest.getAmount());
        if (updateInsuranceRequest.getExpiresIn() != null) insurance.setExpiresIn(updateInsuranceRequest.getExpiresIn());
        if (updateInsuranceRequest.getDuration() != null) insurance.setDuration(updateInsuranceRequest.getDuration());
        if (updateInsuranceRequest.getAgents() != null) {
            Set<InsuranceAgentPermission> agentPermissions = insurance.getAgents();
            updateInsuranceRequest.getAgents().stream()
                    .map(agentRequest -> agentService.findById(agentRequest.getAgentId())
                            .map(agent -> {
                                if (insuranceAgentService.isAgentPermissionExists(insurance, agent)) {
                                    InsuranceAgentPermission permission = insuranceAgentService.findByInsuranceAndAgent(insurance, agent).getFirst();
                                    permission.setPermissions(agentRequest.getPermissions());
                                    insuranceAgentService.save(permission);
                                    return permission;
                                }

                                InsuranceAgentPermission permission = new InsuranceAgentPermission(insurance, agent, agentRequest.getPermissions());
                                insuranceAgentService.save(permission);
                                return permission;
                            })
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .forEach(agentPermissions::add);
            insurance.setAgents(agentPermissions);
        }

        insuranceService.save(insurance);

        return ResponseEntity.ok(new InsuranceResponse(insurance.getId(), insurance.getCompany().getId(), insurance.getName(), insurance.getDescription(),
                insurance.getObjectInsurance(), insurance.getRiskInsurance(), insurance.getConditionsInsurance(),
                insurance.getMaxAmount(), insurance.getAmount(), insurance.getExpiresIn(), insurance.getDuration(), insurance.getAgents()));
    }

    @DeleteMapping("/delete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance deleted successfully!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to delete insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Insurance not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> deleteInsurance(Long insuranceId) {
        // Check if user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        // Check if user has permission to delete insurance
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to delete insurance!"));
        }

        // Check if insurance exists
        if (insuranceService.findById(insuranceId).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Insurance not found!"));
        }

        Insurance insurance = insuranceService.findById(insuranceId).get();

        // Check if user is an agent and has permission to delete insurance
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                agentService.findById(userDetails.getId()).isPresent() &&
                insurance.getAgents().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to delete insurance!"));
        }

        // Check if user is a company and deletes insurance for self
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            if (!insurance.getCompany().getId().equals(userDetails.getId())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to delete insurance!"));
            }
        }

        insuranceService.delete(insurance);

        return ResponseEntity.ok(new MessageResponse("Insurance deleted successfully!"));
    }

    @GetMapping("/getall")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance retrieved successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to get insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getAllInsurance(Long companyId) {
        // Check if user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        // Check if user has permission to get insurances
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to get insurance!"));
        }

        // Get all insurances filtered by company
        return ResponseEntity.ok(insuranceService.findAll().stream().filter(i -> i.getCompany().getId().equals(companyId))
                .map(i -> new InsuranceResponse(i.getId(), i.getCompany().getId(), i.getName(), i.getDescription(),
                        i.getObjectInsurance(), i.getRiskInsurance(), i.getConditionsInsurance(),
                        i.getMaxAmount(), i.getAmount(), i.getExpiresIn(), i.getDuration(), i.getAgents())));
    }
}

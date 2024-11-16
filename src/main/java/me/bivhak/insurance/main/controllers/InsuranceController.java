package me.bivhak.insurance.main.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.bivhak.insurance.main.models.Company;
import me.bivhak.insurance.main.models.EPermission;
import me.bivhak.insurance.main.models.Insurance;
import me.bivhak.insurance.main.payload.request.CreateInsuranceRequest;
import me.bivhak.insurance.main.payload.request.UpdateInsuranceRequest;
import me.bivhak.insurance.main.payload.response.InsuranceResponse;
import me.bivhak.insurance.main.payload.response.MessageResponse;
import me.bivhak.insurance.main.services.CompanyService;
import me.bivhak.insurance.main.services.InsuranceService;
import me.bivhak.insurance.main.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {
    private final CompanyService companyService;
    private final InsuranceService insuranceService;

    public InsuranceController(CompanyService companyService, InsuranceService insuranceService) {
        this.companyService = companyService;
        this.insuranceService = insuranceService;
    }

    @PostMapping("/create")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance created successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to create insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Company not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> createInsurance(CreateInsuranceRequest createInsuranceRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to create insurance!"));
        }

        if (companyService.findById(createInsuranceRequest.getCompanyId()).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Company not found!"));
        }

        Company company = companyService.findById(createInsuranceRequest.getCompanyId()).get();

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                company.getAgentPermissions().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()) &&
                        a.getPermissions().contains(EPermission.CREATE_INSURANCE))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to create insurance!"));
        }

        Insurance insurance = new Insurance(company,
                createInsuranceRequest.getName(), createInsuranceRequest.getDescription(),
                createInsuranceRequest.getTypeInsurance(), createInsuranceRequest.getObjectInsurance(),
                createInsuranceRequest.getRiskInsurance(), createInsuranceRequest.getConditionsInsurance(),
                createInsuranceRequest.getMaxAmount(), createInsuranceRequest.getAmount()
        );

        insuranceService.save(insurance);

        return ResponseEntity.ok(new InsuranceResponse(insurance.getId(), insurance.getName(), insurance.getDescription(),
                insurance.getTypeInsurance(), insurance.getObjectInsurance(), insurance.getRiskInsurance(),
                insurance.getConditionsInsurance(), insurance.getMaxAmount(), insurance.getAmount()));
    }

    @GetMapping("/get")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance retrieved successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to get insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Insurance not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> getInsurance(Long insuranceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to get insurance!"));
        }

        if (insuranceService.findById(insuranceId).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Insurance not found!"));
        }

        Insurance insurance = insuranceService.findById(insuranceId).get();

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                insurance.getCompany().getAgentPermissions().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()) &&
                        a.getPermissions().contains(EPermission.VIEW_INSURANCE))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to get insurance!"));
        }

        return ResponseEntity.ok(new InsuranceResponse(insurance.getId(), insurance.getName(), insurance.getDescription(),
                insurance.getTypeInsurance(), insurance.getObjectInsurance(), insurance.getRiskInsurance(),
                insurance.getConditionsInsurance(), insurance.getMaxAmount(), insurance.getAmount()));
    }

    @PutMapping("/update")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance updated successfully!", content = @Content(schema = @Schema(implementation = InsuranceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to update insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Insurance not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> updateInsurance(UpdateInsuranceRequest updateInsuranceRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to update insurance!"));
        }

        if (insuranceService.findById(updateInsuranceRequest.getCompanyId()).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Insurance not found!"));
        }

        Insurance insurance = insuranceService.findById(updateInsuranceRequest.getCompanyId()).get();

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                insurance.getCompany().getAgentPermissions().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()) &&
                        a.getPermissions().contains(EPermission.UPDATE_INSURANCE))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to update insurance!"));
        }

        if (updateInsuranceRequest.getName() != null) insurance.setName(updateInsuranceRequest.getName());
        if (updateInsuranceRequest.getDescription() != null) insurance.setDescription(updateInsuranceRequest.getDescription());
        if (updateInsuranceRequest.getTypeInsurance() != null) insurance.setTypeInsurance(updateInsuranceRequest.getTypeInsurance());
        if (updateInsuranceRequest.getObjectInsurance() != null) insurance.setObjectInsurance(updateInsuranceRequest.getObjectInsurance());
        if (updateInsuranceRequest.getRiskInsurance() != null) insurance.setRiskInsurance(updateInsuranceRequest.getRiskInsurance());
        if (updateInsuranceRequest.getConditionsInsurance() != null) insurance.setConditionsInsurance(updateInsuranceRequest.getConditionsInsurance());
        if (updateInsuranceRequest.getMaxAmount() != null) insurance.setMaxAmount(updateInsuranceRequest.getMaxAmount());
        if (updateInsuranceRequest.getAmount() != null) insurance.setAmount(updateInsuranceRequest.getAmount());

        insuranceService.save(insurance);

        return ResponseEntity.ok(new InsuranceResponse(insurance.getId(), insurance.getName(), insurance.getDescription(),
                insurance.getTypeInsurance(), insurance.getObjectInsurance(), insurance.getRiskInsurance(),
                insurance.getConditionsInsurance(), insurance.getMaxAmount(), insurance.getAmount()));
    }

    @DeleteMapping("/delete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Insurance deleted successfully!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No user logged in!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: No permission to delete insurance!", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error: Insurance not found!", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> deleteInsurance(Long insuranceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user logged in!"));
        }

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_COMPANY"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to delete insurance!"));
        }

        if (insuranceService.findById(insuranceId).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Insurance not found!"));
        }

        Insurance insurance = insuranceService.findById(insuranceId).get();

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")) &&
                insurance.getCompany().getAgentPermissions().stream().noneMatch(a -> a.getAgent().getId().equals(userDetails.getId()) &&
                        a.getPermissions().contains(EPermission.UPDATE_INSURANCE))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No permission to delete insurance!"));
        }

        insuranceService.delete(insurance);

        return ResponseEntity.ok(new MessageResponse("Insurance deleted successfully!"));
    }
}

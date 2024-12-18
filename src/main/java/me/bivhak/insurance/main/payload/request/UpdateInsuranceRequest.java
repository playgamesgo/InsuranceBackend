package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.Set;

@Getter
@Setter
public class UpdateInsuranceRequest {
    @NotNull(message = "Company id is required")
    private Long companyId;

    @Embedded.Nullable
    private String name;

    @Embedded.Nullable
    private String description;

    @Embedded.Nullable
    private String objectInsurance;

    @Embedded.Nullable
    private String riskInsurance;

    @Embedded.Nullable
    private String conditionsInsurance;

    @Embedded.Nullable
    private Float maxAmount;

    @Embedded.Nullable
    private Float amount;

    @Embedded.Nullable
    private String expiresIn;

    @Embedded.Nullable
    private Float duration;

    @Embedded.Nullable
    private Set<AssignInsuranceAgentRequest> agents;
}

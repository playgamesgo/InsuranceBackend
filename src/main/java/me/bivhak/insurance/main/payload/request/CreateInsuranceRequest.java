package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInsuranceRequest {
    @NotNull
    private Long companyId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String objectInsurance;

    @NotNull
    private String riskInsurance;

    @NotNull
    private String conditionsInsurance;

    @NotNull
    private Float maxAmount;

    @NotNull
    private Float amount;

    @NotNull
    private String expiresIn;

    @NotNull
    private Float duration;

    public CreateInsuranceRequest() {
    }

    public CreateInsuranceRequest(Long companyId, String name, String description, String objectInsurance, String riskInsurance, String conditionsInsurance, Float maxAmount, Float amount, String expiresIn, Float duration) {
        this.companyId = companyId;
        this.name = name;
        this.description = description;
        this.objectInsurance = objectInsurance;
        this.riskInsurance = riskInsurance;
        this.conditionsInsurance = conditionsInsurance;
        this.maxAmount = maxAmount;
        this.amount = amount;
        this.expiresIn = expiresIn;
        this.duration = duration;
    }
}

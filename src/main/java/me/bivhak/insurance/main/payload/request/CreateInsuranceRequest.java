package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInsuranceRequest {
    @NotBlank
    private Long companyId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String objectInsurance;

    @NotBlank
    private String riskInsurance;

    @NotBlank
    private String conditionsInsurance;

    @NotBlank
    private Float maxAmount;

    @NotBlank
    private Float amount;

    @NotBlank
    private String expiresIn;

    @NotBlank
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

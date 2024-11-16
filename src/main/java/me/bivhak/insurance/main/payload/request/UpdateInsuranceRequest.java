package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Embedded;

@Getter
@Setter
public class UpdateInsuranceRequest {
    @NotBlank
    private Long companyId;

    @Embedded.Nullable
    private String name;

    @Embedded.Nullable
    private String description;

    @Embedded.Nullable
    private String typeInsurance;

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

    public UpdateInsuranceRequest() {
    }

    public UpdateInsuranceRequest(Long companyId, String name, String description, String typeInsurance, String objectInsurance, String riskInsurance, String conditionsInsurance, float maxAmount, float amount) {
        this.companyId = companyId;
        this.name = name;
        this.description = description;
        this.typeInsurance = typeInsurance;
        this.objectInsurance = objectInsurance;
        this.riskInsurance = riskInsurance;
        this.conditionsInsurance = conditionsInsurance;
        this.maxAmount = maxAmount;
        this.amount = amount;
    }
}

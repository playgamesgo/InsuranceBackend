package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;
import me.bivhak.insurance.main.models.Insurance;

@Getter
@Setter
public class InsuranceResponse {
    private Long companyId;
    private String name;
    private String description;
    private String typeInsurance;
    private String objectInsurance;
    private String riskInsurance;
    private String conditionsInsurance;
    private float maxAmount;
    private float amount;

    public InsuranceResponse() {
    }

    public InsuranceResponse(Long companyId, String name, String description, String typeInsurance, String objectInsurance, String riskInsurance, String conditionsInsurance, float maxAmount, float amount) {
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

    public static InsuranceResponse fromInsurance(Insurance insurance) {
        return new InsuranceResponse(
                insurance.getCompany().getId(),
                insurance.getName(),
                insurance.getDescription(),
                insurance.getTypeInsurance(),
                insurance.getObjectInsurance(),
                insurance.getRiskInsurance(),
                insurance.getConditionsInsurance(),
                insurance.getMaxAmount(),
                insurance.getAmount()
        );
    }
}

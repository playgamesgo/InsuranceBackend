package me.bivhak.insurance.main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "insurances")
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Company company;

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    @Size(max = 1024)
    private String description;

    @NotBlank
    @Size(max = 64)
    private String typeInsurance;

    @NotBlank
    @Size(max = 64)
    private String objectInsurance;

    @NotBlank
    @Size(max = 64)
    private String riskInsurance;

    @NotBlank
    @Size(max = 64)
    private String conditionsInsurance;

    @NotBlank
    @Size(max = 64)
    private float maxAmount;

    @NotBlank
    @Size(max = 64)
    private float amount;

    public Insurance() {
    }

    public Insurance(Company company, String name, String description, String typeInsurance, String objectInsurance, String riskInsurance, String conditionsInsurance, float maxAmount, float amount) {
        this.company = company;
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

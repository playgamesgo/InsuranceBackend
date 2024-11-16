package me.bivhak.insurance.main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "insurances")
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @NotNull
    @Size(max = 64)
    private String name;

    @NotNull
    @Size(max = 1024)
    private String description;

    @NotNull
    @Size(max = 64)
    private String objectInsurance;

    @NotNull
    @Size(max = 64)
    private String riskInsurance;

    @NotNull
    @Size(max = 64)
    private String conditionsInsurance;

    @NotNull
    private float maxAmount;

    @NotNull
    private float amount;

    @NotNull
    @Size(max = 64)
    private String expiresIn;

    @NotNull
    private float duration;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<InsuranceAgentPermission> agents = Set.of();

    public Insurance() {
    }

    public Insurance(Company company, String name, String description, String objectInsurance, String riskInsurance, String conditionsInsurance, float maxAmount, float amount, String expiresIn, float duration) {
        this.company = company;
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

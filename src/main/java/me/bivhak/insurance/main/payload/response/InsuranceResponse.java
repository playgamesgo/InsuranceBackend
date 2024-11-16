package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;
import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Insurance;
import me.bivhak.insurance.main.models.InsuranceAgentPermission;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class InsuranceResponse {
    private Long id;
    private Long companyId;
    private String name;
    private String description;
    private String objectInsurance;
    private String riskInsurance;
    private String conditionsInsurance;
    private float maxAmount;
    private float amount;
    private String expiresIn;
    private float duration;
    private Set<InsuranceAgentResponse> agents;

    public InsuranceResponse() {
    }

    public InsuranceResponse(Long id, Long companyId, String name, String description, String objectInsurance, String riskInsurance, String conditionsInsurance, float maxAmount, float amount, String expiresIn, float duration, Set<InsuranceAgentPermission> agents) {
        this.id = id;
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
        this.agents = agents.stream().map(agent ->
                new InsuranceAgentResponse(agent.getId(), agent.getAgent().getUsername(), agent.getAgent().getEmail(),
                        agent.getPermissions())).collect(Collectors.toSet());
    }
}

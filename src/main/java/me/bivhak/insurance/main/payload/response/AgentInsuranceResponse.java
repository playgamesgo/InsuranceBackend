package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AgentInsuranceResponse {
    private Long id;
    private String username;
    private String email;
    private Set<InsuranceResponse> insurances;

    public AgentInsuranceResponse(Long id, String username, String email, Set<InsuranceResponse> insurances) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.insurances = insurances;
    }
}

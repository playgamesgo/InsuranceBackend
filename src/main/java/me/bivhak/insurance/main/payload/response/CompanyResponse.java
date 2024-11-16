package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CompanyResponse {
    private Long id;
    private String name;
    private String email;
    private Set<AgentResponse> agents;

    public CompanyResponse(Long id, String name, String email, Set<AgentResponse> agents) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.agents = agents;
    }
}

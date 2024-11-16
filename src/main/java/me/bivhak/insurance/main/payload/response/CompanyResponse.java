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
    private Set<CompanyAgentResponse> agentPermissions;

    public CompanyResponse(Long id, String name, String email, Set<AgentResponse> agents, Set<CompanyAgentResponse> agentPermissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.agents = agents;
        this.agentPermissions = agentPermissions;
    }
}

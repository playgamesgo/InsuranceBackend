package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AgentSelfResponse {
    private Long id;
    private String username;
    private String email;
    private Set<ShortCompanyResponse> companies;

    public AgentSelfResponse(Long id, String username, String email, Set<ShortCompanyResponse> companies) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.companies = companies;
    }
}

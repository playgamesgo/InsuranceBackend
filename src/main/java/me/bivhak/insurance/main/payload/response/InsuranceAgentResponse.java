package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;
import me.bivhak.insurance.main.models.EPermission;

import java.util.Set;

@Getter
@Setter
public class InsuranceAgentResponse {
    private Long id;
    private String username;
    private String email;
    private Set<EPermission> permissions;

    public InsuranceAgentResponse(Long id, String username, String email, Set<EPermission> permissions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.permissions = permissions;
    }
}

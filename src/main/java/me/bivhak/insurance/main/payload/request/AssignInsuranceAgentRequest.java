package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import me.bivhak.insurance.main.models.EPermission;

import java.util.Set;

@Getter
@Setter
public class AssignInsuranceAgentRequest {
    @NotNull
    private Long agentId;

    @NotNull
    private Set<EPermission> permissions;
}

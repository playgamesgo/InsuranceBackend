package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import me.bivhak.insurance.main.models.EPermission;

import java.util.Set;

@Getter
@Setter
public class AssignAgentRequest {
    @NotBlank
    private Long agentId;

    @NotBlank
    private Set<EPermission> permissions;
}

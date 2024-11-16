package me.bivhak.insurance.main.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Embedded;

@Getter
@Setter
public class AssignAgentRequest {
    @Embedded.Nullable
    private Long agentId;

    @Embedded.Nullable
    private String agentName;
}

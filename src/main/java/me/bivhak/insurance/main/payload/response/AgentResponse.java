package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentResponse {
    private Long id;
    private String username;
    private String email;

    public AgentResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}

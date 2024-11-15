package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Response payload for JWT authentication.
 */
@Getter
@Setter
public final class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

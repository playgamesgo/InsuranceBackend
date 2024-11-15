package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	@Setter
    @Getter
    private String refreshToken;
	@Setter
    @Getter
    private Long id;
	@Setter
    @Getter
    private String username;
	@Setter
    @Getter
    private String email;
	@Getter
    private String role;

	public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, String role) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

}

package me.bivhak.insurance.main.payload.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
	@NotNull
	private String username;

	@NotNull
	private String password;

    public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
}

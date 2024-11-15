package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Embedded;

/**
 * Request payload for user signup functionality.
 */
@Setter
@Getter
public final class SignupRequest {
    @NotBlank
    @Size(min = 4, max = 16)
    private String login;

    @NotBlank
    @Size(max = 64)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

}

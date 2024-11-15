package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request payload for reset password functionality.
 */
@Getter
@Setter
public final class ResetPasswordRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}
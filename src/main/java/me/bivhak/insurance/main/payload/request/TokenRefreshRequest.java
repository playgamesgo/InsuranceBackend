package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenRefreshRequest {
  @NotNull
  private String refreshToken;

}

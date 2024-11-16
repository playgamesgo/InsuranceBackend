package me.bivhak.insurance.main.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class SignupRequest {
    @NotNull
    @Size(min = 3, max = 20)
    private String username;
 
    @NotNull
    @Size(max = 50)
    @Email
    private String email;
    
    @NotNull
    @Size(min = 8, max = 40)
    private String password;

}

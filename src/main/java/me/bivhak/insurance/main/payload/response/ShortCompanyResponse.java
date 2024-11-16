package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ShortCompanyResponse {
    private Long id;
    private String name;
    private String email;

    public ShortCompanyResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

package me.bivhak.insurance.main.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import me.bivhak.insurance.main.models.Role;
import me.bivhak.insurance.main.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of UserDetails interface to provide user details for Spring Security.
 */
public final class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter private final Long id;
    @Getter private final String username;
    @Getter private final String email;
    @JsonIgnore @Getter private final String password;
    @Getter private final Role role;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, Role role,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }

    /**
     * Builds a UserDetailsImpl object from a User entity.
     *
     * @param user the User entity.
     * @return a UserDetailsImpl object.
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName().name()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                authorities);
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Checks if this UserDetailsImpl is equal to another object.
     *
     * @param o the object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}

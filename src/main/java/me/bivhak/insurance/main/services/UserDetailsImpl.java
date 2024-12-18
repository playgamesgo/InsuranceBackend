package me.bivhak.insurance.main.services;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Company;
import me.bivhak.insurance.main.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1L;

	@Getter
    private final Long id;

	private final String username;

	@Getter
    private final String email;

	@JsonIgnore
	private final String password;

	private final Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority(user.getRole().getName().name())
		);

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword(), 
				authorities);
	}

	public static UserDetailsImpl build(Agent agent) {
		List<GrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority(agent.getRole().getName().name())
		);

		return new UserDetailsImpl(
				agent.getId(),
				agent.getUsername(),
				agent.getEmail(),
				agent.getPassword(),
				authorities);
	}

	public static UserDetailsImpl build(Company company) {
		List<GrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority(company.getRole().getName().name())
		);

		return new UserDetailsImpl(
				company.getId(),
				company.getUsername(),
				company.getEmail(),
				company.getPassword(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

    @Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

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

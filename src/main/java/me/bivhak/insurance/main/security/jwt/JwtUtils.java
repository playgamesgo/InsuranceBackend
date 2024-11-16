package me.bivhak.insurance.main.security.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.bivhak.insurance.main.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import me.bivhak.insurance.main.services.UserDetailsImpl;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backend.app.jwtSecret}")
    private String jwtSecret = "29aa168e5a284f674b8495c38fde8599088d118705411a3375850078fa6b5791cb8f56b71f7f6cd32c2d57fb9c6c0003049d195c2b61c295cc11940ed13d97836fb508b7d625f7d45a1a00db2767ecc18ffe4198afe3c963d2e1d9d697ed0cc703e5aadbd900a54fcbde2511a7250af04b4930b5c889fc4e09f0a2b196b6749c7c241be2f89f196d2e0e886f7f1f109ca15d66bdfe579cfd2912a4d307380170999d3ca5d48f26be14de28c68d9765e7cfa8f0f6804bc381b73346399726b18b56f455ee64deabd66f598d60eb8d9b0818e773ebc5a1ea8f31145c30d9cd35e3ac6520752a00b83f03f1aed703ebd2900d8e3e86c17fc8b8033f618b36397eec";

    @Value("${backend.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername(), getRolesFromUserDetails(userPrincipal));
    }

    public String generateTokenFromUsername(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs)).
                signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private List<String> getRolesFromUserDetails(UserDetailsImpl userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        List<String> roles = claims.get("roles", List.class);
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Error: User does not have any roles assigned!");
        }
        return roles;
    }

    public boolean validateJwtToken(String authToken) {
        // Attempt to parse and verify the JWT token.
        try {
            Jwts.parser().verifyWith(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}

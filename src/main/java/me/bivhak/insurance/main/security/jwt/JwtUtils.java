package me.bivhak.insurance.main.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.bivhak.insurance.main.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility class for handling JWT operations such as generating, parsing, and validating tokens.
 */
@Component
public final class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backend.app.jwtSecret}")
    private String jwtSecret;

    @Value("${backend.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token based on the authenticated user's details.
     *
     * @param authentication the authentication object containing user details.
     * @return a JWT token as a String.
     */
    public String generateJwtToken(Authentication authentication) {
        // Get the user details from the authentication object.
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Generate the JWT token.
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the secret key used for signing the JWT.
     *
     * @return the secret key.
     */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token.
     * @return the username as a String.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param authToken the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
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
}
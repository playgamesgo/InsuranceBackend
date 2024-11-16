package me.bivhak.insurance.main.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import me.bivhak.insurance.main.exception.TokenRefreshException;
import me.bivhak.insurance.main.models.RefreshToken;
import me.bivhak.insurance.main.repository.RefreshTokenRepository;
import me.bivhak.insurance.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AgentService agentService;
    private final CompanyService companyService;

    @Value("${backend.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, AgentService agentService, CompanyService companyService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.agentService = agentService;
        this.companyService = companyService;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String type, Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        switch (type) {
            case "user" -> refreshToken.setUser(userRepository.findById(userId).get());
            case "agent" -> refreshToken.setAgent(agentService.findById(userId).get());
            case "company" -> refreshToken.setCompany(companyService.findById(userId).get());
        }

        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(String type, Long userId) {
        switch (type) {
            case "user" -> refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
            case "agent" -> refreshTokenRepository.deleteByAgent(agentService.findById(userId).get());
            case "company" -> refreshTokenRepository.deleteByCompany(companyService.findById(userId).get());
        }
    }
}

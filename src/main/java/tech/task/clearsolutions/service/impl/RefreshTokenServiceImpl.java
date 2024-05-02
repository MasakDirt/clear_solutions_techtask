package tech.task.clearsolutions.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.RefreshToken;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.exception.TokenRefreshException;
import tech.task.clearsolutions.repository.RefreshTokenRepository;
import tech.task.clearsolutions.service.RefreshTokenService;
import tech.task.clearsolutions.service.UserService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${refresh.token.millis}")
    private long refreshTokenMillis;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public RefreshToken createRefreshToken(String email) {
        var user = userService.getByEmail(email);
        if (isUserHasRefreshToken(user)) {
            log.info("User already has a refresh token!");
            return user.getRefreshToken();
        }
        return createNewRefreshToken(user);
    }

    @Override
    public boolean isUserHasRefreshToken(User user) {
        return refreshTokenRepository.findByUserInfo(user).isPresent();
    }

    private RefreshToken createNewRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenMillis))
                .build();
        log.info("Created refresh token for user with email: {}", user.getEmail());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> getByToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token);
        log.info("Getting refresh token for user with token: {}", token);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            log.info("Refresh token has expired!");
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

}

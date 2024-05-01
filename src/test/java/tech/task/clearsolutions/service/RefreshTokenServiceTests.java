package tech.task.clearsolutions.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.RefreshToken;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RefreshTokenServiceTests {

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Autowired
    public RefreshTokenServiceTests(RefreshTokenService refreshTokenService,
                                    RefreshTokenRepository refreshTokenRepository,
                                    UserService userService) {
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Test
    public void testCreateRefreshTokenSuccess_Spring() {
        String email = "janesmith@example.com";
        User userInfo = userService.getByEmail(email);
        RefreshToken expectedRefreshToken = new RefreshToken();
        expectedRefreshToken.setUserInfo(userInfo);
        expectedRefreshToken.setExpiryDate(Instant.now().plusMillis(600000));

        RefreshToken actualRefreshTOken = refreshTokenService.createRefreshToken(email);
        expectedRefreshToken.setId(actualRefreshTOken.getId());

        assertEquals(expectedRefreshToken, actualRefreshTOken);
        assertEquals(expectedRefreshToken.getUserInfo(), actualRefreshTOken.getUserInfo());
    }

    @Test
    public void testCreateRefreshTokenUserNotFound_Spring() {
        assertThrows(EntityNotFoundException.class, () ->
                refreshTokenService.createRefreshToken("invalid"));
    }

    @Test
    public void testGetByTokenValid_Spring() {
        String token = UUID.randomUUID().toString();
        RefreshToken expectedRefreshToken = new RefreshToken();
        expectedRefreshToken.setToken(token);
        expectedRefreshToken.setExpiryDate(Instant.now());
        refreshTokenRepository.save(expectedRefreshToken);

        RefreshToken actualRefreshToken = refreshTokenService.getByToken(token);

        assertEquals(expectedRefreshToken, actualRefreshToken);
    }

    @Test
    public void testGetByTokenNotFound_Spring() {
        assertThrows(EntityNotFoundException.class, () ->
                refreshTokenService.getByToken("invalid"));
    }

}

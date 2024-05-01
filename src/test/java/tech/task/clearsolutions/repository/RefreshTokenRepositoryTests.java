package tech.task.clearsolutions.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.RefreshToken;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RefreshTokenRepositoryTests {
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenRepositoryTests(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Test
    public void testFindByTokenSuccess() {
        String token = UUID.randomUUID().toString();
        RefreshToken expectedRefreshToken = new RefreshToken();
        expectedRefreshToken.setToken(token);
        expectedRefreshToken.setExpiryDate(Instant.now());
        refreshTokenRepository.save(expectedRefreshToken);

        Optional<RefreshToken> actualRefreshToken = refreshTokenRepository.findByToken(token);

        assertTrue(actualRefreshToken.isPresent());
        assertEquals(expectedRefreshToken, actualRefreshToken.get());
    }

    @Test
    public void testFindByTokeNotFound() {
        Optional<RefreshToken> actualRefreshToken = refreshTokenRepository.findByToken("invalid");

        assertTrue(actualRefreshToken.isEmpty());
    }

}

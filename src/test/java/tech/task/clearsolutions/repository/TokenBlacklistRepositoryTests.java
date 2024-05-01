package tech.task.clearsolutions.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.TokenBlacklist;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TokenBlacklistRepositoryTests {
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public TokenBlacklistRepositoryTests(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Test
    public void testPresentFindByToken() {
        String token = "must be a valid token";

        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklistRepository.save(tokenBlacklist);

        assertTrue(tokenBlacklistRepository.findByToken(token).isPresent());
    }

    @Test
    public void testEmptyFindByToken() {
        String token = "invalid token";

        assertTrue(tokenBlacklistRepository.findByToken(token).isEmpty());
    }

}

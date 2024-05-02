package tech.task.clearsolutions.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.repository.TokenBlacklistRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TokenBlacklistServiceTests {
    private final TokenBlacklistService tokenBlacklistService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public TokenBlacklistServiceTests(TokenBlacklistService tokenBlacklistService,
                                      TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Test
    public void testAddTokenToBlacklistSuccess() {
        int tokensSizeBeforeAdding = tokenBlacklistRepository.findAll().size();
        String newToken = generateNewUUIDTokenAndAddItToBlacklist();
        int tokensSizeAfterAdding = tokenBlacklistRepository.findAll().size();

        assertTrue(tokenBlacklistRepository.findByToken(newToken).isPresent());
        assertTrue(tokensSizeAfterAdding > tokensSizeBeforeAdding);
    }

    @Test
    public void testTokenBlacklistedTrue() {
        String newToken = generateNewUUIDTokenAndAddItToBlacklist();
        assertTrue(tokenBlacklistService.isTokenBlacklisted(newToken));
    }

    private String generateNewUUIDTokenAndAddItToBlacklist() {
        String newToken = UUID.randomUUID().toString();
        tokenBlacklistService.addTokenToBlacklist(newToken);

        return newToken;
    }

    @Test
    public void testTokenBlacklistedFalse() {
        String newToken = "new not existing token";
        assertFalse(tokenBlacklistService.isTokenBlacklisted(newToken));
    }

}

package tech.task.clearsolutions.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.TokenBlacklist;
import tech.task.clearsolutions.repository.TokenBlacklistRepository;
import tech.task.clearsolutions.service.TokenBlacklistService;

@Slf4j
@Service
@AllArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public void addTokenToBlacklist(String token) {
        tokenBlacklistRepository.save(TokenBlacklist.of(token));
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token).isPresent();
    }

}

package tech.task.clearsolutions.service;

import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.RefreshToken;
import tech.task.clearsolutions.domain.User;

import java.util.Optional;

@Service
public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    Optional<RefreshToken> getByToken(String token);

    boolean isUserHasRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

}

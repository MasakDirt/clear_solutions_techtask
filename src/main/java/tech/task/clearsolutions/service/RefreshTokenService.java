package tech.task.clearsolutions.service;

import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.RefreshToken;
import tech.task.clearsolutions.domain.User;

@Service
public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    RefreshToken getByToken(String token);

    boolean isUserHasRefreshToken(User user);

}

package tech.task.clearsolutions.service;

import org.springframework.stereotype.Service;

@Service
public interface TokenBlacklistService {

    void addTokenToBlacklist(String token);

    boolean isTokenBlacklisted(String token);

}

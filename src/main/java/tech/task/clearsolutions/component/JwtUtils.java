package tech.task.clearsolutions.component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Component
public class JwtUtils {
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${inspiration.ms}")
    private long inspirationMs;

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(currentTimeMillis() + inspirationMs))
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isJwtTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setAllowedClockSkewSeconds(2)
                    .setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            log.error("Invalid JWT token: {}", malformedJwtException.getMessage());
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("JWT token is expired: {}", expiredJwtException.getMessage());
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.error("JWT token is unsupported: {}", unsupportedJwtException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            log.error("JWT claims string is empty: {}", illegalArgumentException.getMessage());
        } catch (SignatureException signatureException) {
            log.error("JWT signature does not match locally computed signature: {}", signatureException.getMessage());
        }

        return false;
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(1)
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    protected Key getKey() {
        return KEY;
    }

}

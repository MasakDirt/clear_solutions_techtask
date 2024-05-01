package tech.task.clearsolutions.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTests {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Value("${my.inspiration.ms}")
    private long inspirationMs;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTokenFromEmail() {
        String email = "test@example.com";

        String token = jwtUtils.generateTokenFromEmail(email);
        assertNotNull(token);

        long currentTime = System.currentTimeMillis();
        long expectedExpiration = currentTime + inspirationMs;

        Date expirationDate = parseClaimsJws(token)
                .getBody()
                .getExpiration();

        int roundingDigits = 100000;
        long roundedExpectedExpiration = roundTime(expectedExpiration, roundingDigits);
        long roundedActualExpiration = roundTime(expirationDate.getTime(), roundingDigits);


        assertEquals(roundedExpectedExpiration, roundedActualExpiration);
    }

    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(1)
                .setSigningKey(jwtUtils.getKey())
                .build()
                .parseClaimsJws(token);
    }

    private long roundTime(long value, int roundedDigit) {
        return (Math.round((double) value / roundedDigit) * roundedDigit);
    }

    @Test
    void isJwtTokenValid() {
        String email = "some@mail.co";
        String validToken = generateTokenFromUsername(email);
        String invalidToken = validToken + "invalid";

        assertTrue(jwtUtils.isJwtTokenValid(validToken));

        assertFalse(jwtUtils.isJwtTokenValid(invalidToken));
    }

    @Test
    void getSubject() {
        String email = "some@mail.co";
        String token = generateTokenFromUsername(email);

        assertEquals(email, jwtUtils.getSubject(token));
    }

    private String generateTokenFromUsername(String email) {
       return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(currentTimeMillis() + inspirationMs))
                .signWith(jwtUtils.getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

}

package tech.task.clearsolutions.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.task.clearsolutions.TestAdvice.getViolation;
import static tech.task.clearsolutions.TestAdvice.testInvalidField;

@SpringBootTest
@ActiveProfiles("test")
public class RefreshTokenTests {

    private RefreshToken refreshToken;

    @BeforeEach
    public void setRefreshToken() {
        refreshToken = new RefreshToken(1L, "new token", Instant.now(), new User());
    }

    @Test
    public void testValidRefreshToken() {
        assertEquals(0, getViolation(refreshToken).size());
    }

    @ParameterizedTest
    @MethodSource("emptyOrNullArgs")
    public void testInvalidToken(String token) {
        refreshToken.setToken(token);
        testInvalidField(refreshToken, token,
                "Refresh token cannot be null or empty!");
    }

    private static Stream<String> emptyOrNullArgs() {
        return Stream.of("", null);
    }

    @Test
    public void testNullExpiryDate() {
        refreshToken.setExpiryDate(null);
        testInvalidField(refreshToken, null,
                "Expiry day mustn't be null!");
    }
}


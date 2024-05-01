package tech.task.clearsolutions.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.task.clearsolutions.TestAdvice.getViolation;
import static tech.task.clearsolutions.TestAdvice.testInvalidField;

@SpringBootTest
@ActiveProfiles("test")
public class TokenBlacklistTests {
    private TokenBlacklist tokenBlacklist;

    @BeforeEach
    public void setTokenBlacklist() {
        tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setId(1L);
        tokenBlacklist.setToken("Some token");
    }

    @Test
    public void testValidTokenBlacklist() {
        assertEquals(0, getViolation(tokenBlacklist).size());
    }

    @ParameterizedTest
    @MethodSource("emptyAndNullArguments")
    public void testInvalidToken(String token) {
        tokenBlacklist.setToken(token);
        testInvalidField(tokenBlacklist, token,
                "Token can't be empty!");
    }

    private static Stream<String> emptyAndNullArguments() {
        return Stream.of("", null);
    }
}

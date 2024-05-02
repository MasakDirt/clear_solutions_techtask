package tech.task.clearsolutions.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.task.clearsolutions.TestAdvice;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.task.clearsolutions.TestAdvice.getViolation;

@SpringBootTest
@ActiveProfiles("test")
public class RoleTests {
    private Role role;

    @BeforeEach
    public void setRole() {
        role = new Role();
        role.setId(2L);
        role.setName("NEW");
    }

    @Test
    public void testValidRole() {
        assertEquals(0, getViolation(role).size());
    }

    @ParameterizedTest
    @MethodSource("invalidRoleNames")
    public void testInvalidRoleName(String name) {
        role.setName(name);
        TestAdvice.testInvalidField(role, name,
                "Role name must be written and in upper case format.");
    }

    private static Stream<String> invalidRoleNames() {
        return Stream.of("RoLE", "ROLe", "rOLE", "", null);
    }

}

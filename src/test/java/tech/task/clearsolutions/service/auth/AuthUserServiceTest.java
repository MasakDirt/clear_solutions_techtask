package tech.task.clearsolutions.service.auth;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class AuthUserServiceTest {
    private final AuthUserService authUserService;
    private final UserService userService;

    @Autowired
    public AuthUserServiceTest(AuthUserService authUserService, UserService userService) {
        this.authUserService = authUserService;
        this.userService = userService;
    }

    @Test
    public void testIsUsersSameTrue() {
        String email = "bobjohnson@example.com";
        User user = userService.getByEmail(email);

        assertTrue(authUserService.isUserSame(user.getId(), email));
    }

    @Test
    public void testIsUsersSameNotFound() {
        String email = "bobjohnson@example.com";
        long id = 0;

        assertThrows(EntityNotFoundException.class, () -> authUserService.isUserSame(id, email));
    }

}

package tech.task.clearsolutions.service.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.task.clearsolutions.service.UserService;

@Service
@AllArgsConstructor
public class AuthUserService {
    private final UserService userService;

    public boolean isUserSame(Long id, String email) {
        return userService.getById(id).getEmail().equals(email);
    }

}

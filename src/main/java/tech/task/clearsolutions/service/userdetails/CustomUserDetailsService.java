package tech.task.clearsolutions.service.userdetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.task.clearsolutions.service.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userService.getByEmail(email);
        log.info("Load user by email: {}", email);
        return user;
    }
}

package tech.task.clearsolutions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.task.clearsolutions.service.UserService;
import tech.task.clearsolutions.service.userdetails.CustomUserDetailsService;

@Configuration
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

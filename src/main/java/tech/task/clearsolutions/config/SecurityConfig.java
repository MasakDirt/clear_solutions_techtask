package tech.task.clearsolutions.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.task.clearsolutions.component.AuthTokenFilter;
import tech.task.clearsolutions.component.EntryPointJwt;
import tech.task.clearsolutions.exception.AuthenticationException;
import tech.task.clearsolutions.exception.TokenFilterException;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final EntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        authorizeRequests(httpSecurity);
        addingTokenFilterAndEntryPoint(httpSecurity);
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    private void authorizeRequests(HttpSecurity httpSecurity) {
        try {
            httpSecurity.authorizeHttpRequests(request ->
                    request.requestMatchers("/api/v1/auth/login", "/api/v1/auth/signup",
                                    "/api/v1/auth/refresh-token", "/swagger-ui/**",
                                    "/v3/api-docs/**").permitAll()
                            .anyRequest()
                            .authenticated());
        } catch (Exception e) {
            log.error("Exception while user authorize - 'authorizeRequests'");
            throw new AuthenticationException("You have not permission to go here," +
                    " please login correctly");
        }
    }

    private void addingTokenFilterAndEntryPoint(HttpSecurity httpSecurity) {
        try {
            httpSecurity.exceptionHandling(handling ->
                            handling.authenticationEntryPoint(authEntryPointJwt))
                    .addFilterBefore(authenticationTokenFilter,
                            UsernamePasswordAuthenticationFilter.class);
        } catch (Exception e) {
            log.error("Exception while adding filter before - 'addingTokenFilterAndEntryPoint");
            throw new TokenFilterException("It`s absolutely our mistake, so sorry," +
                    " we can fix it in a few minutes");
        }
    }

}

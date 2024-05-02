package tech.task.clearsolutions.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.task.clearsolutions.exception.TokenExpiredException;
import tech.task.clearsolutions.service.TokenBlacklistService;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String HEADER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        checkIfHeaderHasTokenAndItsValidAndNotBlocked(request);
        filterChain.doFilter(request, response);
    }

    private void checkIfHeaderHasTokenAndItsValidAndNotBlocked(HttpServletRequest request) {
        if (hasAuthorizationBearer(request)) {
            checkForValidAndAccessibleToken(getAccessToken(request), request);
        }
    }

    private void checkForValidAndAccessibleToken(String accessToken, HttpServletRequest request) {
        if (jwtUtils.isJwtTokenValid(accessToken)) {
            ifTokenBlacklistedThrowException(accessToken);
            setAuthContext(accessToken, request);
        }
    }

    private void ifTokenBlacklistedThrowException(String accessToken) {
        if (tokenBlacklistService.isTokenBlacklisted(accessToken)) {
            throw new TokenExpiredException("Token has expired or been revoked, please re-login!");
        }
    }

    public String checkIfExistAndGetToken(HttpServletRequest request) {
        if (hasAuthorizationBearer(request)) {
            return getAccessToken(request);
        }

        throw new TokenExpiredException("Your token is expired, re-login please!");
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = getHeaderFromRequest(request);
        return Objects.nonNull(header) && header.startsWith(HEADER_PREFIX);
    }

    private String getAccessToken(HttpServletRequest request) {
        return getHeaderFromRequest(request).substring(HEADER_PREFIX.length());
    }

    private String getHeaderFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private void setAuthContext(String token, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                generateUsernamePasswordAuthenticationToken(token);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken generateUsernamePasswordAuthenticationToken(String token) {
        var userDetails = userDetailsService.loadUserByUsername(jwtUtils.getSubject(token));

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                null, userDetails.getAuthorities());
    }

}

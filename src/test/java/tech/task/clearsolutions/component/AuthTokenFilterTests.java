package tech.task.clearsolutions.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.task.clearsolutions.domain.Role;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.exception.TokenExpiredException;
import tech.task.clearsolutions.service.TokenBlacklistService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthTokenFilterTests {
    @InjectMocks
    private AuthTokenFilter authTokenFilter;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternal_Success() throws ServletException, IOException {
        String token = "Some token";
        String email = "new@mail.co";

        Role role = new Role();
        role.setName("NAME");

        User user = new User();
        user.setRole(role);
        user.setEmail(email);

        when(jwtUtils.isJwtTokenValid(anyString())).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted(anyString())).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.getSubject(anyString())).thenReturn(email);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);


        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).isJwtTokenValid(anyString());
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(anyString());
        verify(request, times(2)).getHeader(anyString());
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(jwtUtils, times(1)).getSubject(anyString());
    }

    @Test
    public void testDoFilterInternal_TokenInvalid() throws ServletException, IOException {
        String token = "Some token";

        when(jwtUtils.isJwtTokenValid(anyString())).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);


        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).isJwtTokenValid(anyString());
        verify(tokenBlacklistService, times(0)).isTokenBlacklisted(anyString());
        verify(request, times(2)).getHeader(anyString());
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(jwtUtils, times(0)).getSubject(anyString());
    }

    @Test
    public void testDoFilterInternal_HeaderNotContainsToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");


        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(0)).isJwtTokenValid(anyString());
        verify(tokenBlacklistService, times(0)).isTokenBlacklisted(anyString());
        verify(request, times(1)).getHeader(anyString());
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(jwtUtils, times(0)).getSubject(anyString());
    }

    @Test
    public void testDoFilterInternal_TokenBlacklisted() {
        String token = "Tokenoken";

        when(jwtUtils.isJwtTokenValid(anyString())).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted(anyString())).thenReturn(true);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        Assertions.assertThrows(TokenExpiredException.class, () ->
                authTokenFilter.doFilterInternal(request, response, filterChain));

        verify(jwtUtils, times(1)).isJwtTokenValid(anyString());
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(anyString());
        verify(request, times(2)).getHeader(anyString());
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(jwtUtils, times(0)).getSubject(anyString());
    }

    @Test
    public void testCheckIfExistAndGetToken_Success() {
        String expectedToken = "newToken";

        when(request.getHeader(anyString())).thenReturn("Bearer " + expectedToken);
        String actualToken = authTokenFilter.checkIfExistAndGetToken(request);

        Assertions.assertEquals(expectedToken, actualToken);

        verify(request, times(2)).getHeader(anyString());
    }

    @Test
    public void testCheckIfExistAndGetToken_TokenEmpty() {
        when(request.getHeader(anyString())).thenReturn("");

        Assertions.assertThrows(TokenExpiredException.class, () ->
                authTokenFilter.checkIfExistAndGetToken(request));

        verify(request, times(1)).getHeader(anyString());
    }

}

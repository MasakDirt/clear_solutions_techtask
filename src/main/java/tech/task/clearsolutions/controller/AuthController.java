package tech.task.clearsolutions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.task.clearsolutions.component.JwtUtils;
import tech.task.clearsolutions.dto.*;
import tech.task.clearsolutions.mapper.UserMapper;
import tech.task.clearsolutions.service.RefreshTokenService;
import tech.task.clearsolutions.service.UserService;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP,
        scheme = "bearer", bearerFormat = "JWT")
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Operation(summary = "Login form")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserTokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "BadRequest",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NotFound",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        var user = userService.proceedLogin(loginRequest);
        String email = user.getEmail();
        String accessToken = jwtUtils.generateTokenFromEmail(email);
        userService.setRefreshToken(user, refreshTokenService.createRefreshToken(email));
        log.debug("LOGIN === with email: {}, timestamp: {}", email, LocalDateTime.now());

        return ResponseEntity
                .ok(userMapper.getTokenResponseFromDomain(user, accessToken));
    }

    @Operation(summary = "Sign Up form")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Sign up",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "BadRequest",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid
                                                   UserCreateRequest createRequest) {
        var user = userService.createUser(userMapper.getDomainFromCreateRequest(createRequest));
        log.debug("REGISTER-USER === with email: {}, timestamp: {}",
                user.getEmail(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.getResponseFromDomain(user));
    }

}

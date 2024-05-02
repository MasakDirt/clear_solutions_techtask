package tech.task.clearsolutions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.task.clearsolutions.component.AuthTokenFilter;
import tech.task.clearsolutions.dto.ErrorResponse;
import tech.task.clearsolutions.service.TokenBlacklistService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/logout")
@SecurityRequirement(name = "Bearer Authentication")
public class LogoutController {
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthTokenFilter authTokenFilter;

    @Operation(summary = "Logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful logout",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public ResponseEntity<?> logout(HttpServletRequest request, Authentication authentication) {
        tokenBlacklistService.addTokenToBlacklist(this.extractTokenFromRequest(request));
        log.debug("User with email {}, successfully logged out!", authentication.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("You have successfully logged out!");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return authTokenFilter.checkIfExistAndGetToken(request);
    }

}

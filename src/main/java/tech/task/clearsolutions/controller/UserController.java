package tech.task.clearsolutions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.task.clearsolutions.dto.ErrorResponse;
import tech.task.clearsolutions.dto.UserResponse;
import tech.task.clearsolutions.dto.UserSpecialFieldsUpdate;
import tech.task.clearsolutions.dto.UserUpdateRequest;
import tech.task.clearsolutions.mapper.UserMapper;
import tech.task.clearsolutions.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Update special fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful special fields update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "BadRequest",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NotFound",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/special-fields")
    @PreAuthorize("@authUserService.isUserSame(#specialFieldsUpdate.id, authentication.name)")
    public ResponseEntity<UserResponse> updateUserFields(
            @RequestBody @Valid UserSpecialFieldsUpdate specialFieldsUpdate) {
        var user = userService.updateSpecialFields(specialFieldsUpdate);
        log.debug("PATCH-USER-SPECIAL_FIELDS === with id - {}, timestamp - {}",
                specialFieldsUpdate.getId(), LocalDateTime.now());

        return ResponseEntity.ok(userMapper.getResponseFromDomain(user));
    }

    @Operation(summary = "Update all fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful all fields update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "BadRequest",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NotFound",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping
    @PreAuthorize("@authUserService.isUserSame(#userUpdateRequest.id, authentication.name)")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid
                                                   UserUpdateRequest userUpdateRequest) {
        var user = userService.updateFullUser(
                userMapper.getDomainFromUpdateRequest(userUpdateRequest),
                userUpdateRequest.getOldPassword());
        log.debug("PUT-FULL_USER === with id - {}, timestamp - {}",
                user.getId(), LocalDateTime.now());

        return ResponseEntity.ok(userMapper.getResponseFromDomain(user));
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful delete user",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NotFound",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authUserService.isUserSame(#id, authentication.name)")
    public void deleteUser(@PathVariable Long id) {
        log.debug("DELETE-USER === with id - {}, timestamp - {}", id, LocalDateTime.now());
        userService.deleteUser(id);
    }

    @Operation(summary = "Get by dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get users by dates",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "400", description = "BadRequest",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/dates")
    public ResponseEntity<List<UserResponse>> getUsersByDates(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        var users = userService.getAllUsersByDates(from, to);
        log.debug("GET-USERS-BY-DATES === from - {}, to - {}", from, to);

        return ResponseEntity.ok(userMapper.getResponseListFromDomain(users));
    }

}

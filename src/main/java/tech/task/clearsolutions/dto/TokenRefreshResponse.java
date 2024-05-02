package tech.task.clearsolutions.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {

    @NotEmpty(message = "Access token must be included!")
    private String accessToken;

    @NotEmpty(message = "Refresh token must be included!")
    private String refreshToken;

}

package tech.task.clearsolutions.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {

    @NotEmpty(message = "Refresh token must be included!")
    public String refreshToken;

}

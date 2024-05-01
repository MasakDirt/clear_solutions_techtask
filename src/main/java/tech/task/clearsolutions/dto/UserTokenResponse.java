package tech.task.clearsolutions.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenResponse {

    private Long id;

    @NotNull(message = "Must be a valid e-mail address")
    @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}",
            message = "Must be a valid e-mail address")
    private String email;

    @NotEmpty(message = "Fill in your first name please!")
    private String firstname;

    @NotEmpty(message = "Fill in your last name please!")
    private String lastname;

    @NotEmpty(message = "Access token can't be empty!\n" +
            "Sorry it's our mistake re login please")
    private String accessToken;

    @NotEmpty(message = "Refresh token can't be empty!\n" +
            "Sorry it's our mistake re login please")
    private String refreshToken;

}

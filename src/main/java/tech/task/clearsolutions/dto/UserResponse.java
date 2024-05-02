package tech.task.clearsolutions.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {

    private Long id;

    @NotNull(message = "Must be a valid e-mail address")
    @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}",
            message = "Must be a valid e-mail address")
    private String email;

    @NotEmpty(message = "Fill in your first name please!")
    private String firstname;

    @NotEmpty(message = "Fill in your last name please!")
    private String lastname;

    @NotNull(message = "Birth date must be selected!")
    private LocalDate birthDate;

    private String city;

    private String street;

    private String number;

    private String apartmentNumber;

    @Pattern(regexp="^$|\\+(?:[0-9]●?){10,11}[0-9]$",
            message="Please provide a valid phone number")
    private String phoneNumber;

}

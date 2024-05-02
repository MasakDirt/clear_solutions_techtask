package tech.task.clearsolutions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import tech.task.clearsolutions.domain.Address;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(of = "email")
public class UserCreateRequest {

    @NotNull(message = "Must be a valid e-mail address")
    @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}",
            message = "Must be a valid e-mail address")
    private String email;

    @NotEmpty(message = "Fill in your first name please!")
    private String firstname;

    @NotEmpty(message = "Fill in your last name please!")
    private String lastname;

    @NotEmpty(message = "Password must be included!")
    private String password;

    @NotNull(message = "Birth date must be selected!")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    private Address address;

    @Pattern(regexp="^$|\\+(?:[0-9]●?){10,11}[0-9]$",
            message="Please provide a valid phone number")
    private String phoneNumber;

}

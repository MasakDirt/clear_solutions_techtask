package tech.task.clearsolutions.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.task.clearsolutions.exception.IllegalBirthDateValue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "users")
@ToString(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Must be a valid e-mail address")
    @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}",
            message = "Must be a valid e-mail address")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @NotEmpty(message = "Fill in your first name please!")
    private String firstname;

    @Column(nullable = false)
    @NotEmpty(message = "Fill in your last name please!")
    private String lastname;

    @Column(nullable = false)
    @NotEmpty(message = "Password must be included!")
    private String password;

    @Column(name = "birth_date", nullable = false)
    @NotNull(message = "Birth date must be selected!")
    private LocalDate birthDate;

    @Embedded
    private Address address;

    @Column
    @Pattern(regexp="^$|\\+(?:[0-9]●?){10,11}[0-9]$",
            message="Please provide a valid phone number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
    private RefreshToken refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "Your role is not identified!")
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void checkForValidBirthDate() {
        if (birthDate != null && isBirthDateEqualsToNowOrAfterToday()) {
            throw new IllegalBirthDateValue("Are you really birth in that day?)");
        }
    }

    private boolean isBirthDateEqualsToNowOrAfterToday() {
        LocalDate today = LocalDate.now();
        return birthDate.equals(today) || birthDate.isAfter(today);
    }

    public void isUserReachedRequiredAge(int requiredAge) {
        LocalDate usersAge = birthDate.plusYears(requiredAge);
        if (isUsersAgeNotPermitted(usersAge)) {
            throw new IllegalBirthDateValue("You are not yet of the right age!" +
                    " You must be at least '" + requiredAge + "' years old.");
        }
    }

    private boolean isUsersAgeNotPermitted(LocalDate usersAge) {
        // if users age is before today it's okay, because user reached required age
        return !usersAge.isBefore(LocalDate.now());
    }

    public void mapUserSpecialFields(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public void setUsersRoleAndRefreshToken(Role role, RefreshToken refreshToken) {
        this.role = role;
        this.refreshToken = refreshToken;
    }

}

package tech.task.clearsolutions.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.task.clearsolutions.TestAdvice.getViolation;
import static tech.task.clearsolutions.TestAdvice.testInvalidField;

@SpringBootTest
@ActiveProfiles("test")
public class UserTests {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstname("First");
        user.setLastname("Last");
        user.setEmail("maksymkorniev88@gmail.com");
        user.setAddress(new Address());
        user.setId(2L);
        user.setPassword("password");
        user.setRole(new Role());
        user.setRefreshToken(new RefreshToken());
        user.setPhoneNumber("+380688624050");
        user.setBirthDate(LocalDate.of(2004, 11, 8));
    }

    @Test
    public void testValidUser() {
        assertEquals(0, getViolation(user).size());
    }


    @ParameterizedTest
    @MethodSource("emptyAndNullArguments")
    public void testInvalidFirstName(String firstName) {
        user.setFirstname(firstName);
        testInvalidField(user, firstName,
                "Fill in your first name please!");
    }

    @ParameterizedTest
    @MethodSource("emptyAndNullArguments")
    public void testInvalidLastName(String lastName) {
        user.setLastname(lastName);
        testInvalidField(user, lastName,
                "Fill in your last name please!");
    }

    @ParameterizedTest
    @MethodSource("emptyAndNullArguments")
    public void testInvalidPassword(String password) {
        user.setPassword(password);
        testInvalidField(user, password,
                "Password must be included!");
    }

    private static Stream<String> emptyAndNullArguments() {
        return Stream.of("", null);
    }

    @ParameterizedTest
    @MethodSource("argumentsForEmail")
    public void testInvalidEmail(String email) {
        user.setEmail(email);
        testInvalidField(user, email,
                "Must be a valid e-mail address");
    }

    private static Stream<String> argumentsForEmail() {
        return Stream.of("", "email", "ema@il",
                "user@.", "user@mail", "user@mai,co", "user@mail.comomo", null);
    }

    @Test
    public void testInvalidBirthDate() {
        user.setBirthDate(null);
        testInvalidField(user, null,
                "Birth date must be selected!");
    }

    @ParameterizedTest
    @MethodSource("argumentsForPhoneNumber")
    public void testInvalidPhoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        testInvalidField(user, phoneNumber,
                "Please provide a valid phone number");
    }

    private static Stream<String> argumentsForPhoneNumber() {
        return Stream.of("+jdjddj", "355677", "5676889",
                "+467890-87654323", "+3454676879099543");
    }

    @Test
    public void testRoleNull() {
        user.setRole(null);
        testInvalidField(user, null,
                "Your role is not identified!");
    }

}

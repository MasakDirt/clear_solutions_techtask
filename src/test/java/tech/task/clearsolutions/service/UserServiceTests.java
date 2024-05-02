package tech.task.clearsolutions.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import tech.task.clearsolutions.domain.Address;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.dto.LoginRequest;
import tech.task.clearsolutions.dto.UserSpecialFieldsUpdate;
import tech.task.clearsolutions.exception.IllegalBirthDateValue;
import tech.task.clearsolutions.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceTests(UserService userService, UserRepository userRepository,
                            RoleService roleService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Test
    public void testCreateUserSuccess() {
        User expected = getUserWithBirthDate(LocalDate.of(2000, 1, 1));
        User actual = userService.createUser(expected);
        expected.setId(actual.getId());

        assertEquals(expected, actual);
        assertEquals("USER", actual.getRole().getName());
    }

    @Test
    public void testCreateUserIncorrectBirthDate() {
        assertThrows(IllegalBirthDateValue.class, () ->
                userService.createUser(getUserWithBirthDate(LocalDate.now().plusYears(1))));
    }

    @Test
    public void testCreateUserIncorrectAgeValue() {
        assertThrows(IllegalBirthDateValue.class, () ->
                userService.createUser(getUserWithBirthDate(LocalDate.of(2022, 1, 1))));
    }

    @Test
    public void testUpdateSpecialFieldsSuccess() {
        User expected = userService.createUser(getUserWithBirthDate(LocalDate.of(2000, 1, 1)));
        String oldEmail = expected.getEmail();
        String oldFirstName = expected.getFirstname();
        String oldLastName = expected.getLastname();
        UserSpecialFieldsUpdate userUpdateRequest = UserSpecialFieldsUpdate.builder()
                .id(expected.getId())
                .email("new@mail.co")
                .firstname("Updated")
                .lastname("Updated")
                .build();
        User actual = userService.updateSpecialFields(userUpdateRequest);

        assertNotEquals(oldFirstName, actual.getFirstname());
        assertNotEquals(oldEmail, actual.getEmail());
        assertNotEquals(oldLastName, actual.getLastname());
    }

    @Test
    public void testUpdateSpecialFieldsNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
                userService.updateSpecialFields(UserSpecialFieldsUpdate.builder().id(0L).build()));
    }

    private User getUserWithBirthDate(LocalDate localDate) {
        User user = new User();
        user.setFirstname("Nazar");
        user.setLastname("Kostyk");
        user.setEmail("nazar.kostyk@gmail.com");
        user.setPassword("1234");
        user.setRole(roleService.getByName("USER"));
        user.setAddress(new Address());
        user.setBirthDate(localDate);
        user.setPhoneNumber("+380688624050");

        return user;
    }

    @Test
    public void testDeleteSuccess() {
        int sizeBeforeDeleting = userRepository.findAll().size();
        userService.deleteUser(1L);
        int sizeAfterDeleting = userRepository.findAll().size();

        assertTrue(sizeBeforeDeleting > sizeAfterDeleting);
    }

    @Test
    public void testDeleteNotFound() {
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(0L));
    }

    @Test
    public void testGetByIdSuccess() {
        User expected = userRepository.findAll()
                .stream()
                .findFirst()
                .get();
        User actual = userService.getById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByIdFailure() {
        assertThrows(EntityNotFoundException.class, () -> userService.getById(0L));
    }

    @Test
    public void testGetByEmailSuccess() {
        User expected = userRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new User());
        User actual = userService.getByEmail(expected.getEmail());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByEmailFailure() {
        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail("nope"));
    }

    @Test
    public void testGetAllUsersByDates() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2022, 1, 1);
        List<User> expected = userRepository.findAll()
                .stream()
                .filter(user -> user.getBirthDate().isAfter(from) &&
                        user.getBirthDate().isBefore(to))
                .toList();

        List<User> actual = userService.getAllUsersByDates(from, to);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllUsersByDatesFromIsBeforeTo() {
        LocalDate from = LocalDate.of(2022, 1, 1);
        LocalDate to = LocalDate.of(1990, 1, 1);

        assertThrows(IllegalArgumentException.class, () ->
                userService.getAllUsersByDates(from, to));
    }

    @Test
    public void testUpdateFullUserSuccess() {
        String expectedPhone = "+5678987545";
        String expectedEmail = "new@mail.co";
        User expected = getUserWithBirthDate(LocalDate.of(2000, 12, 2));
        expected.setId(2L);
        expected.setPhoneNumber(expectedPhone);
        expected.setEmail(expectedEmail);

        User actual = userService.updateFullUser(expected, "1234");

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expectedPhone, actual.getPhoneNumber());
        assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    public void testUpdateFullUserNotFound() {
        User user = getUserWithBirthDate(LocalDate.of(2000, 12, 1));
        user.setId(0L);

        assertThrows(EntityNotFoundException.class, () ->
                userService.updateFullUser(user, "1234"));
    }


    @Test
    public void testUpdateFullUserInvalidOldPassword() {
        User user = getUserWithBirthDate(LocalDate.of(2000, 12, 1));
        user.setId(2L);

        assertThrows(ResponseStatusException.class, () ->
                userService.updateFullUser(user, "invalid"));
    }

    @Test
    public void testUpdateFullUserInvalidBirthdateValue() {
        User user = getUserWithBirthDate(LocalDate.now().plusYears(2));
        user.setId(2L);

        assertThrows(IllegalBirthDateValue.class, () ->
                userService.updateFullUser(user, "1234"));
    }

    @Test
    public void testUpdateFullUserInvalidAgeValue() {
        User user = getUserWithBirthDate(LocalDate.of(2020, 12, 1));
        user.setId(2L);

        assertThrows(IllegalBirthDateValue.class, () ->
                userService.updateFullUser(user, "1234"));
    }

    @Test
    public void testProceedLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("makskorniev@example.com")
                .password("1234")
                .build();

        User expected = userService.getByEmail(loginRequest.getEmail());
        User actual = userService.proceedLogin(loginRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void testProceedLoginWithInvalidPassword() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("makskorniev@example.com")
                .password("invalid")
                .build();

        assertThrows(ResponseStatusException.class, () -> userService.proceedLogin(loginRequest));
    }

}

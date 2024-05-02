package tech.task.clearsolutions.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.dto.UserResponse;
import tech.task.clearsolutions.dto.UserSpecialFieldsUpdate;
import tech.task.clearsolutions.dto.UserUpdateRequest;
import tech.task.clearsolutions.mapper.UserMapper;
import tech.task.clearsolutions.repository.UserRepository;
import tech.task.clearsolutions.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tech.task.clearsolutions.TestAdvice.asJsonString;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = MOCK)
public class UserControllerTests {
    public static final String BASIC_URL = "/api/v1/user";

    private final MockMvc mockMvc;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserControllerTests(MockMvc mockMvc, UserService userService,
                               UserMapper userMapper, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Test
    @WithMockUser(username = "makskorniev@example.com")
    public void testUpdateUserFieldsSuccess() throws Exception {
        String email = "makskorniev@example.com";
        User user = userService.getByEmail(email);
        Long id = user.getId();
        UserSpecialFieldsUpdate specialFieldsUpdate = createNewSpecialFieldsUpdate(
                id, "First", "Last", "new@mail.co");

        String actual = performUpdateUserFields(specialFieldsUpdate)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        user.setFirstname(specialFieldsUpdate.getFirstname());
        user.setLastname(specialFieldsUpdate.getLastname());
        user.setEmail(specialFieldsUpdate.getEmail());
        UserResponse expected = userMapper.getResponseFromDomain(user);

        assertEquals(asJsonString(expected), actual);
    }

    @Test
    @WithMockUser(username = "bobjohnson@example.com")
    public void testUpdateUserFieldsBadRequest() throws Exception {
        String email = "bobjohnson@example.com";
        Long id = userService.getByEmail(email).getId();
        UserSpecialFieldsUpdate specialFieldsUpdate = createNewSpecialFieldsUpdate(
                id, "", "", "user@mail.co");

        performUpdateUserFields(specialFieldsUpdate)
                .andExpect(status().isBadRequest())
                .andExpectAll(result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("\"status\":\"BAD_REQUEST\"")),
                        result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("Fill in your last name please!")),
                        result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("Fill in your first name please!")));
    }

    @Test
    @WithMockUser(username = "bobjohnson@example.com")
    public void testUpdateUserFieldsForbidden() throws Exception {
        String email = "makskorniev@example.com";
        Long id = userService.getByEmail(email).getId();
        UserSpecialFieldsUpdate specialFieldsUpdate = createNewSpecialFieldsUpdate(
                id, "First", "Last", "unauthorized@mail.co");

        performUpdateUserFields(specialFieldsUpdate)
                .andExpect(matcherForbiddenStatus())
                .andExpect(matcherForbiddenResult());
    }

    @Test
    @WithMockUser(username = "makskorniev@example.com")
    public void testUpdateUserFieldsNotFound() throws Exception {
        UserSpecialFieldsUpdate specialFieldsUpdate = createNewSpecialFieldsUpdate(
                0L, "First", "Last", "unauthorized@mail.co");

        performUpdateUserFields(specialFieldsUpdate)
                .andExpect(matcherNotFoundStatus())
                .andExpect(matcherNotFoundResult());
    }

    private UserSpecialFieldsUpdate createNewSpecialFieldsUpdate(Long id, String firstname,
                                                                 String lastname, String email) {
        return UserSpecialFieldsUpdate.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .build();
    }

    private ResultActions performUpdateUserFields(
            UserSpecialFieldsUpdate specialFieldsUpdate) throws Exception {
        return mockMvc.perform(patch(BASIC_URL + "/special-fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(specialFieldsUpdate)));
    }

    @Test
    @WithMockUser("johndoe@example.com")
    public void testUpdateUserSuccess() throws Exception {
        String email = "johndoe@example.com";
        User user = userService.getByEmail(email);
        String newFirstName = "Name first";
        LocalDate birthDate = LocalDate.of(1990, 2, 12);
        String password = "new";
        UserUpdateRequest userUpdateRequest = createUpdateRequest(
                user, newFirstName, birthDate, password);

        String actualUser = performUpdateUser(userUpdateRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        user.setFirstname(newFirstName);
        user.setBirthDate(birthDate);
        user.setPassword(password);

        UserResponse expectedUser = userMapper.getResponseFromDomain(user);
        assertEquals(asJsonString(expectedUser), actualUser);
    }

    @Test
    @WithMockUser("johndoe@example.com")
    public void testUpdateUserBadRequest() throws Exception {
        String email = "johndoe@example.com";
        User user = userService.getByEmail(email);
        UserUpdateRequest userUpdateRequest = createUpdateRequest(
                user, null, null, "password");

        performUpdateUser(userUpdateRequest)
                .andExpect(status().isBadRequest())
                .andExpectAll(result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("\"status\":\"BAD_REQUEST\"")),
                        result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("Birth date must be selected!")),
                        result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("Fill in your first name please!")));
    }

    @Test
    @WithMockUser("johndoe@example.com")
    public void testUpdateUserUnauthorizedInvalidPassword() throws Exception {
        String email = "johndoe@example.com";
        User user = userService.getByEmail(email);
        UserUpdateRequest userUpdateRequest = createUpdateRequest(
                user, "Name", LocalDate.now().minusYears(1), "password");
        userUpdateRequest.setOldPassword("invalid");

        performUpdateUser(userUpdateRequest)
                .andExpect(status().isUnauthorized())
                .andExpectAll(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"UNAUTHORIZED\"," +
                                "\"message\":\"Invalid password, please re-write it!\"")));
    }

    @Test
    @WithMockUser(username = "bobjohnson@example.com")
    public void testUpdateUserForbidden() throws Exception {
        String email = "janesmith@example.com";
        User user = userService.getByEmail(email);
        UserUpdateRequest updateRequest = createUpdateRequest(
                user, "Name", LocalDate.now().minusYears(1), "password");

        performUpdateUser(updateRequest)
                .andExpect(matcherForbiddenStatus())
                .andExpect(matcherForbiddenResult());
    }

    @Test
    @WithMockUser(username = "bobjohnson@example.com")
    public void testUpdateUserNotFound() throws Exception {
        String email = "bobjohnson@example.com";
        User user = userService.getByEmail(email);
        UserUpdateRequest updateRequest = createUpdateRequest(
                user, "Name", LocalDate.now().minusYears(1), "password");
        updateRequest.setId(0L);

        performUpdateUser(updateRequest)
                .andExpect(matcherNotFoundStatus())
                .andExpect(matcherNotFoundResult());
    }

    private UserUpdateRequest createUpdateRequest(
            User user, String firstname, LocalDate birthDate, String password) {
        return UserUpdateRequest.builder()
                .address(user.getAddress())
                .email(user.getEmail())
                .id(user.getId())
                .lastname(user.getLastname())
                .firstname(firstname)
                .birthDate(birthDate)
                .oldPassword("1234")
                .newPassword(password)
                .build();
    }

    private ResultActions performUpdateUser(UserUpdateRequest userUpdateRequest) throws Exception {
        return mockMvc.perform(put(BASIC_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userUpdateRequest)));
    }

    @Test
    @WithMockUser("janesmith@example.com")
    public void testDeleteSuccess() throws Exception {
        Long id = userService.getByEmail("janesmith@example.com").getId();
        int sizeBeforeDeleting = userRepository.findAll().size();

        performDelete(id)
                .andExpect(status().isNoContent());

        int sizeAfterDeleting = userRepository.findAll().size();
        assertTrue(sizeBeforeDeleting > sizeAfterDeleting);
    }

    @Test
    @WithMockUser("janesmith@example.com")
    public void testDeleteForbidden() throws Exception {
        Long id = userService.getByEmail("bobjohnson@example.com").getId();

        performDelete(id)
                .andExpect(matcherForbiddenStatus())
                .andExpect(matcherForbiddenResult());
    }

    private ResultMatcher matcherForbiddenStatus() {
        return status().isForbidden();
    }

    private ResultMatcher matcherForbiddenResult() {
        return result -> assertTrue(result.getResponse().getContentAsString()
                .contains("\"status\":\"FORBIDDEN\",\"message\":\"Access Denied\""));
    }

    @Test
    @WithMockUser("bobjohnson@example.com")
    public void testDeleteNotFound() throws Exception {
        Long id = 0L;

        performDelete(id)
                .andExpect(matcherNotFoundStatus())
                .andExpect(matcherNotFoundResult());
    }

    private ResultActions performDelete(Long id) throws Exception {
        return mockMvc.perform(delete(BASIC_URL + "/{id}", id));
    }

    private ResultMatcher matcherNotFoundStatus() {
        return status().isNotFound();
    }

    private ResultMatcher matcherNotFoundResult() {
        return result -> assertTrue(result.getResponse().getContentAsString()
                .contains("\"status\":\"NOT_FOUND\"," +
                        "\"message\":\"User with id 0 not found, " +
                        "please check if it valid!\""));
    }

    @Test
    @WithMockUser("makskorniev@example.com")
    public void testGetByDatesSuccess() throws Exception {
        LocalDate from = LocalDate.of(1989, 6, 22);
        LocalDate to = LocalDate.of(2020, 2, 1);

        String actual = performGetByDates(from, to)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<UserResponse> expectedList = userService.getAllUsersByDates(from, to)
                .stream()
                .map(userMapper::getResponseFromDomain)
                .toList();

        assertEquals(asJsonString(expectedList), actual);
    }

    @Test
    @WithMockUser("johndoe@example.com")
    public void testGetByDatesBadRequestFromIsAfterTo() throws Exception {
        LocalDate from = LocalDate.of(2100, 9, 3);
        LocalDate to = LocalDate.of(1999, 12, 21);

        performGetByDates(from, to)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"BAD_REQUEST\"," +
                                "\"message\":\"Date 'from' must goes before date 'to'\"")));
    }

    private ResultActions performGetByDates(LocalDate from, LocalDate to) throws Exception {
        return mockMvc.perform(get(BASIC_URL + "/dates")
                .param("from", from.toString())
                .param("to", to.toString()));
    }

}

package tech.task.clearsolutions.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.domain.Address;
import tech.task.clearsolutions.dto.LoginRequest;
import tech.task.clearsolutions.dto.UserCreateRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tech.task.clearsolutions.TestAdvice.asJsonString;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = MOCK)
public class AuthControllerTests {
    private static final String BASIC_URL = "/api/v1/auth";

    private final MockMvc mockMvc;

    @Autowired
    public AuthControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String email = "johndoe@example.com";
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("1234")
                .build();

        mockMvc.perform(post(BASIC_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andExpectAll(result -> assertFalse(result.getResponse().getContentAsString()
                                .contains("\"accessToken\":\"null\"")),
                        result -> assertFalse(result.getResponse().getContentAsString()
                                .contains("\"refreshToken\":\"null\"")),
                        result -> assertTrue(result.getResponse().getContentAsString()
                                .contains("\"email\":\"" + email + "\"")));
    }

    @Test
    public void testLoginNotFound() throws Exception {
        String email = "notFound@example.com";
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("1234")
                .build();

        mockMvc.perform(post(BASIC_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"NOT_FOUND\","
                                + "\"message\":\"User with email " + email
                                + " not found, please check if it valid!\"")));
    }

    @Test
    public void testLoginInvalidPassword() throws Exception {
        String email = "johndoe@example.com";
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("invalid")
                .build();

        mockMvc.perform(post(BASIC_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"UNAUTHORIZED\","
                                + "\"message\":\"Invalid password, please re-write it!\"")));
    }

    @Test
    public void testLoginBadRequest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .build();

        mockMvc.perform(post(BASIC_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"BAD_REQUEST\","
                                + "\"message\":\"Must be a valid e-mail address, "
                                + "Password must be included!\"")));
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserCreateRequest userCreateRequest = createUserRequest(
                LocalDate.of(2004, 11, 8), "+380688624050");

        mockMvc.perform(post(BASIC_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"email\":\"newUser@mail.co\"," +
                                "\"firstname\":\"First\",\"lastname\":\"Last\"")));
    }

    @Test
    public void testRegisterUserInvalidData() throws Exception {
        UserCreateRequest userCreateRequest = createUserRequest(
                LocalDate.of(2004, 11, 8), "+6849942");

        mockMvc.perform(post(BASIC_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"BAD_REQUEST\","
                                + "\"message\":\"Please provide a valid phone number\"")));
    }

    @Test
    public void testRegisterUserInvalidBirthdate() throws Exception {
        UserCreateRequest userCreateRequest = createUserRequest(
                LocalDate.now().plusYears(12), "+380688624050");

        mockMvc.perform(post(BASIC_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"BAD_REQUEST\","
                                + "\"message\":\"Are you really birth in that day?)\"")));
    }

    @Test
    public void testRegisterUserInvalidAge() throws Exception {
        UserCreateRequest userCreateRequest = createUserRequest(
                LocalDate.of(2012, 4, 20), "+380688624050");

        mockMvc.perform(post(BASIC_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"BAD_REQUEST\","
                                + "\"message\":\"You are not yet of the right age! "
                                + "You must be at least '18' years old.\"")));
    }

    private UserCreateRequest createUserRequest(LocalDate localDate, String phoneNumber) {
        return UserCreateRequest.builder()
                .email("newUser@mail.co")
                .firstname("First")
                .lastname("Last")
                .phoneNumber(phoneNumber)
                .password("new pass")
                .birthDate(localDate)
                .address(new Address())
                .build();
    }

}

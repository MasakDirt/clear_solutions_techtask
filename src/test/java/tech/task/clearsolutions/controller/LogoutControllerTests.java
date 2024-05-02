package tech.task.clearsolutions.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.task.clearsolutions.repository.TokenBlacklistRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = MOCK)
public class LogoutControllerTests {
    private final MockMvc mockMvc;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public LogoutControllerTests(MockMvc mockMvc,
                                 TokenBlacklistRepository tokenBlacklistRepository) {
        this.mockMvc = mockMvc;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testLogoutSuccess_Spring() throws Exception {
        int tokensBeforeLogout = tokenBlacklistRepository.findAll().size();
        mockMvc.perform(post("/api/v1/logout")
                        .header("Authorization", "Bearer JDfdjfjdksgfgndngfdg"))
                .andExpect(status().isNoContent())
                .andExpect(result -> assertEquals("You have successfully logged out!",
                        result.getResponse().getContentAsString()));
        int tokensAfterLogout = tokenBlacklistRepository.findAll().size();

        assertTrue(tokensBeforeLogout < tokensAfterLogout);
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testLogoutNullTokenExc_Spring() throws Exception {
        mockMvc.perform(post("/api/v1/logout"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains("\"status\":\"INTERNAL_SERVER_ERROR\"," +
                                "\"message\":\"Your token is expired, re-login please!\"")));
    }

}

package com.ryan.dontapdabomb.dontapdabomb.GameTests;

import com.ryan.dontapdabomb.dontapdabomb.entity.User;
import com.ryan.dontapdabomb.dontapdabomb.repository.UserRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GameIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User createUser(String name, String rawPassword, int cash) {
        User user = new User();
        user.setName(name);
        user.setPassword(new BCryptPasswordEncoder().encode(rawPassword));
        user.setCash(cash);
        return userRepository.save(user);
    }

    @Test
    void startGame_success() throws Exception {
        // Arrange: user bestaat
        createUser("testuser2", "correctpw", 100);

        String requestBody = """
                {
                  "username": "testuser2",
                  "password": "correctpw",
                  "boardSize": 9,
                  "numMines": 2,
                  "betAmount": 10
                }
                """;

        // Act + Assert
        mockMvc.perform(post("/games/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.betAmount").value(10));
    }

    @Test
    void startGame_wrongPassword_returns400() throws Exception {
        createUser("testuser2", "correctpw", 100);

        String requestBody = """
                {
                  "username": "testuser2",
                  "password": "WRONG",
                  "boardSize": 9,
                  "numMines": 2,
                  "betAmount": 10
                }
                """;

        mockMvc.perform(post("/games/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Wrong password"));
    }


    @Test
    void startGame_notEnoughCash_returns400() throws Exception {
        createUser("pooruser", "pw123", 5);

        String requestBody = """
                {
                  "username": "pooruser",
                  "password": "pw123",
                  "boardSize": 9,
                  "numMines": 2,
                  "betAmount": 10
                }
                """;

        mockMvc.perform(post("/games/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Not enough cash"));
    }
}



package feenysh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feenysh.dto.LoginRequest;
import feenysh.dto.RegisterRequest;
import feenysh.model.User;
import feenysh.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rollback transactions after each test
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Clear the repository if needed, or rely on @Transactional
        userRepository.deleteAll();
    }

    @Test
    void registerUser_success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setDisplayName("New User");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User registered successfully! Please login.")));

        assertTrue(userRepository.findByUsername("newuser").isPresent());
    }

    @Test
    void registerUser_usernameTaken() throws Exception {
        // Arrange: Create an existing user
        User existingUser = new User("existinguser", passwordEncoder.encode("password"), "existing@example.com", "Existing");
        userRepository.save(existingUser);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existinguser"); // Same username
        registerRequest.setEmail("newemail@example.com");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Username is already taken!")));
    }

    @Test
    void registerUser_emailTaken() throws Exception {
        User existingUser = new User("anotheruser", passwordEncoder.encode("password"), "common@example.com", "Another");
        userRepository.save(existingUser);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("freshuser");
        registerRequest.setEmail("common@example.com"); // Same email
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Email Address already in use!")));
    }


    @Test
    void loginUser_success() throws Exception {
        // Arrange: Register a user first
        User user = new User("testlogin", passwordEncoder.encode("password123"), "testlogin@example.com", "Test Login");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testlogin");
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.username", is("testlogin")))
                .andReturn();

        // Optionally, validate the token structure or claims if necessary (more advanced)
        String responseString = result.getResponse().getContentAsString();
        assertTrue(responseString.contains("accessToken"));
    }

    @Test
    void loginUser_invalidCredentials_usernameNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                 .andExpect(jsonPath("$.message", containsString("Login failed: Invalid username or password.")));
    }

    @Test
    void loginUser_invalidCredentials_wrongPassword() throws Exception {
        User user = new User("testloginwrongpass", passwordEncoder.encode("password123"), "testloginwrongpass@example.com", "Test WrongPass");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testloginwrongpass");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("Login failed: Invalid username or password.")));
    }
}

package com.faircart.integration;

import com.faircart.dto.auth.AuthResponse;
import com.faircart.dto.auth.LoginRequest;
import com.faircart.dto.auth.RegisterRequest;
import com.faircart.entity.User;
import com.faircart.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.faircart.FairCartApplication.class, properties = {
        "spring.autoconfigure.exclude=org.redisson.spring.starter.RedissonAutoConfigurationV2,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_BASE = "/api/v1/auth";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password("SecurePass123!")
                .phone("+1234567890")
                .build();

        MvcResult result = mockMvc.perform(post(API_BASE + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.user.fullName").value("John Doe"))
                .andExpect(jsonPath("$.data.user.role").value("CUSTOMER"))
                .andReturn();

        // Verify user is saved in database
        User savedUser = userRepository.findByEmail("john.doe@example.com").orElseThrow();
        assertThat(savedUser.getFullName()).isEqualTo("John Doe");
        assertThat(savedUser.getPhone()).isEqualTo("+1234567890");
        assertThat(savedUser.getRole()).isEqualTo(User.Role.CUSTOMER);
        assertThat(passwordEncoder.matches("SecurePass123!", savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("Should reject registration with duplicate email")
    void shouldRejectDuplicateEmail() throws Exception {
        // Create existing user
        User existingUser = User.builder()
                .email("existing@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .fullName("Existing User")
                .phone("+1987654321")
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
        userRepository.save(existingUser);

        RegisterRequest request = RegisterRequest.builder()
                .fullName("New User")
                .email("existing@example.com")
                .password("NewPass123!")
                .phone("+1234567890")
                .build();

        mockMvc.perform(post(API_BASE + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already registered: existing@example.com"));
    }

    @Test
    @DisplayName("Should reject registration with duplicate phone")
    void shouldRejectDuplicatePhone() throws Exception {
        User existingUser = User.builder()
                .email("existing@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .fullName("Existing User")
                .phone("+1987654321")
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
        userRepository.save(existingUser);

        RegisterRequest request = RegisterRequest.builder()
                .fullName("New User")
                .email("new@example.com")
                .password("NewPass123!")
                .phone("+1987654321")
                .build();

        mockMvc.perform(post(API_BASE + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Phone number already registered: +1987654321"));
    }

    @Test
    @DisplayName("Should reject registration with invalid email")
    void shouldRejectInvalidEmail() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("John Doe")
                .email("invalid-email")
                .password("SecurePass123!")
                .phone("+1234567890")
                .build();

        mockMvc.perform(post(API_BASE + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject registration with short password")
    void shouldRejectShortPassword() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .password("short")
                .phone("+1234567890")
                .build();

        mockMvc.perform(post(API_BASE + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void shouldLoginSuccessfully() throws Exception {
        // Create user
        User user = User.builder()
                .email("login@example.com")
                .password(passwordEncoder.encode("LoginPass123!"))
                .fullName("Login User")
                .phone("+15551234567")
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("login@example.com")
                .password("LoginPass123!")
                .build();

        MvcResult result = mockMvc.perform(post(API_BASE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.user.email").value("login@example.com"))
                .andExpect(jsonPath("$.data.user.fullName").value("Login User"))
                .andReturn();

        // Verify token can be used
        String responseBody = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).at("/data/accessToken").asText();
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("Should reject login with invalid password")
    void shouldRejectInvalidPassword() throws Exception {
        User user = User.builder()
                .email("login2@example.com")
                .password(passwordEncoder.encode("CorrectPass123!"))
                .fullName("Login User")
                .phone("+15551234568")
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("login2@example.com")
                .password("WrongPass123!")
                .build();

        mockMvc.perform(post(API_BASE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should reject login for non-existent user")
    void shouldRejectNonExistentUser() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("AnyPass123!")
                .build();

        mockMvc.perform(post(API_BASE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should reject login for disabled user")
    void shouldRejectDisabledUser() throws Exception {
        User user = User.builder()
                .email("disabled@example.com")
                .password(passwordEncoder.encode("Pass123!"))
                .fullName("Disabled User")
                .phone("+15559999999")
                .role(User.Role.CUSTOMER)
                .enabled(false)
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("disabled@example.com")
                .password("Pass123!")
                .build();

        mockMvc.perform(post(API_BASE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Account is disabled"));
    }

    @Test
    @DisplayName("Should get current user with valid token")
    void shouldGetCurrentUserWithValidToken() throws Exception {
        User user = User.builder()
                .email("me@example.com")
                .password(passwordEncoder.encode("MePass123!"))
                .fullName("Me User")
                .phone("+15558888888")
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
        userRepository.save(user);

        // Login first
        LoginRequest loginRequest = LoginRequest.builder()
                .email("me@example.com")
                .password("MePass123!")
                .build();

        MvcResult loginResult = mockMvc.perform(post(API_BASE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .at("/data/accessToken").asText();

        // Use token to get current user
        mockMvc.perform(get(API_BASE + "/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("me@example.com"))
                .andExpect(jsonPath("$.data.fullName").value("Me User"))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }

    @Test
    @DisplayName("Should reject access with invalid token")
    void shouldRejectInvalidToken() throws Exception {
        mockMvc.perform(get(API_BASE + "/me")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should reject access without token")
    void shouldRejectWithoutToken() throws Exception {
        mockMvc.perform(get(API_BASE + "/me"))
                .andExpect(status().isUnauthorized());
    }
}
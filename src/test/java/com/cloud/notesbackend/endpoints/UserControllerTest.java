package com.cloud.notesbackend.endpoints;

import com.cloud.notesbackend.entities.User;
import com.cloud.notesbackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(null);
    }

    @Test
    void register_Success() throws Exception {
        String request =
                """
                        {
                            "username": "user",
                            "password": "12345678"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User created successfully")));
    }

    @Test
    void register_BlankData() throws Exception {
        String request =
                """
                        {
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_InvalidLength() throws Exception {
        String request =
                """
                        {
                            "username": "u",
                            "password": "1234567"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_UserAlreadyExists() throws Exception {
        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(new User());

        String request =
                """
                        {
                            "username": "user",
                            "password": "12345678"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User already exists")));
    }

    @Test
    void login_Success() throws Exception {
        User user = new User();

        user.setUsername("user");
        user.setRole("ROLE_USER");

        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        String request =
                """
                        {
                            "username": "user",
                            "password": "12345678"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

    @Test
    void login_UserNotFound() throws Exception {
        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(null);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        String request =
                """
                        {
                            "username": "user",
                            "password": "12345678"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User does not exist")));
    }

    @Test
    void login_BlankData() throws Exception {
        User user = new User();

        user.setUsername("user");
        user.setRole("ROLE_USER");

        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        String request =
                """
                        {
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_InvalidLength() throws Exception {
        User user = new User();

        user.setUsername("user");
        user.setRole("ROLE_USER");

        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        String request =
                """
                        {
                            "username": "u",
                            "password": "1234567"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_InvalidPassword() throws Exception {
        User user = new User();

        user.setUsername("user");
        user.setRole("ROLE_USER");

        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);

        String request =
                """
                        {
                            "username": "user",
                            "password": "12345678"
                        }
                        """;

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Incorrect password")));
    }
}
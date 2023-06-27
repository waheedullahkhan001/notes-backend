package com.cloud.notesbackend.services;

import com.cloud.notesbackend.entities.User;
import com.cloud.notesbackend.exceptions.BadRequestException;
import com.cloud.notesbackend.exceptions.NotFoundException;
import com.cloud.notesbackend.repositories.UserRepository;
import com.cloud.notesbackend.requests.LoginRequest;
import com.cloud.notesbackend.requests.RegistrationRequest;
import com.cloud.notesbackend.responses.BasicResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUser_UserAlreadyExists() {
        Mockito.when(
                userRepository.findUserByUsername(Mockito.anyString())
        ).thenReturn(
                new User()
        );

        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("user");
        request.setPassword("12345678");

        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.createUser(request)
        );
        Assertions.assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void createUser_UserCreated() {
        Mockito.when(
                userRepository.findUserByUsername(Mockito.anyString())
        ).thenReturn(
                null
        );

        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("user");
        request.setPassword("12345678");

        BasicResponse basicResponse = userService.createUser(request);

        Assertions.assertTrue(basicResponse.getSuccess());
        Assertions.assertEquals("User created successfully", basicResponse.getMessage());
    }


    @Test
    public void authenticateUser_UserNotFound() {
        Mockito.when(
                userRepository.findUserByUsername(Mockito.anyString())
        ).thenReturn(
                null
        );

        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("12345678");

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.loginUser(request)
        );
        Assertions.assertEquals("User does not exist", exception.getMessage());
    }

    @Test
    public void authenticateUser_IncorrectPassword() {
        User user = new User();

        user.setPassword("abcdefgh");

        Mockito.when(
                userRepository.findUserByUsername(Mockito.anyString())
        ).thenReturn(
                user
        );

        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("12345678");

        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.loginUser(request)
        );
        Assertions.assertEquals("Incorrect password", exception.getMessage());
    }

    @Test
    public void authenticateUser_Authenticated() {
        User user = new User();

        user.setPassword("12345678");

        Mockito.when(
                userRepository.findUserByUsername(Mockito.anyString())
        ).thenReturn(
                user
        );

        Mockito.when(
                passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(
                true
        );

        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("12345678");

        Assertions.assertDoesNotThrow(() -> userService.loginUser(request));
    }

}

package com.cloud.notesbackend.services;

import com.cloud.notesbackend.entities.User;
import com.cloud.notesbackend.exceptions.BadRequestException;
import com.cloud.notesbackend.exceptions.NotFoundException;
import com.cloud.notesbackend.repositories.UserRepository;
import com.cloud.notesbackend.requests.LoginRequest;
import com.cloud.notesbackend.requests.RegistrationRequest;
import com.cloud.notesbackend.responses.BasicResponse;
import com.cloud.notesbackend.responses.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public BasicResponse createUser(RegistrationRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername());

        if (user != null) {
            throw new BadRequestException("User already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        user = request.toUser();
        user.setRole("USER");

        userRepository.save(user);

        return new BasicResponse(true, "User created successfully");
    }

    public BasicResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername());

        if (user == null) {
            throw new NotFoundException("User does not exist");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }

        return new LoginResponse(
                true,
                "User authenticated successfully",
                jwtService.generateToken(user.getUsername(), user.getRole())
        );
    }

}

package com.cloud.notesbackend.endpoints;

import com.cloud.notesbackend.requests.LoginRequest;
import com.cloud.notesbackend.requests.RegistrationRequest;
import com.cloud.notesbackend.responses.BasicResponse;
import com.cloud.notesbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public BasicResponse register(@Valid @RequestBody RegistrationRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public BasicResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.authenticateUser(request);
    }

}

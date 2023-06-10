package com.cloud.notesbackend.requests;

import com.cloud.notesbackend.entities.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    private String username;
    private String password;

    // TODO: Use model mapper
    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

}

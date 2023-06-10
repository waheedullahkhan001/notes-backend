package com.cloud.notesbackend.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends BasicResponse {

    private String token;

    public LoginResponse(Boolean success, String message, String token) {
        super(success, message);
        this.token = token;
    }

}

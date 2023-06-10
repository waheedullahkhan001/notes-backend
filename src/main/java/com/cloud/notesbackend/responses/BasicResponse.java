package com.cloud.notesbackend.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasicResponse {

    private Boolean success;
    private String message;

}

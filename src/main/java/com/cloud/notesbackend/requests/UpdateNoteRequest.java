package com.cloud.notesbackend.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNoteRequest {
    @NotBlank
    private String title;
    @NotNull
    private String content;
}

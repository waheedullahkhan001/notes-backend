package com.cloud.notesbackend.requests;

import com.cloud.notesbackend.entities.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNoteRequest {

    @NotBlank
    private String title;
    @NotNull
    private String content;

    public Note toNote() {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        return note;
    }
}

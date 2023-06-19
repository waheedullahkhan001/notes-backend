package com.cloud.notesbackend.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNoteResponse {
    public Long id;
    public String title;
    public String content;
}

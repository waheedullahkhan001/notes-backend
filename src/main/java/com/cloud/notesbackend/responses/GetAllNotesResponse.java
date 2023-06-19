package com.cloud.notesbackend.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllNotesResponse {

    public List<GetNoteResponse> notes;
}

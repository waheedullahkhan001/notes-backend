package com.cloud.notesbackend.endpoints;

import com.cloud.notesbackend.requests.CreateNoteRequest;
import com.cloud.notesbackend.requests.UpdateNoteRequest;
import com.cloud.notesbackend.responses.BasicResponse;
import com.cloud.notesbackend.responses.GetAllNotesResponse;
import com.cloud.notesbackend.services.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/create")
    public BasicResponse createNote(@Valid @RequestBody CreateNoteRequest request) {
        return noteService.createNote(request);
    }

    @GetMapping("/all")
    public GetAllNotesResponse getAllNotes() {
        return noteService.getAllNotes();
    }

    @PutMapping("/{id}")
    public BasicResponse updateNote(@PathVariable Long id, @Valid @RequestBody UpdateNoteRequest request) {
        return noteService.updateNote(id, request);
    }

    @DeleteMapping("/{id}")
    public BasicResponse deleteNote(@PathVariable Long id) {
        return noteService.deleteNote(id);
    }
}

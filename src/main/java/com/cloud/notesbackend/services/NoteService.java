package com.cloud.notesbackend.services;

import com.cloud.notesbackend.entities.Note;
import com.cloud.notesbackend.entities.User;
import com.cloud.notesbackend.repositories.NoteRepository;
import com.cloud.notesbackend.repositories.UserRepository;
import com.cloud.notesbackend.requests.CreateNoteRequest;
import com.cloud.notesbackend.responses.BasicResponse;
import com.cloud.notesbackend.responses.GetAllNotesResponse;
import com.cloud.notesbackend.responses.GetNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public BasicResponse createNote(CreateNoteRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);

        Note note = request.toNote();
        note.setUser(user);
        noteRepository.save(note);

        return new BasicResponse(true, "Note created successfully");
    }

    public GetAllNotesResponse getAllNotes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Note> notes = noteRepository.findAllByUserUsername(username);

        return new GetAllNotesResponse(notes.stream().map(note ->
                new GetNoteResponse(note.getId(), note.getTitle(), note.getContent())
        ).toList());
    }
}

package com.cloud.notesbackend.services;

import com.cloud.notesbackend.entities.Note;
import com.cloud.notesbackend.entities.User;
import com.cloud.notesbackend.exceptions.BadRequestException;
import com.cloud.notesbackend.repositories.NoteRepository;
import com.cloud.notesbackend.repositories.UserRepository;
import com.cloud.notesbackend.requests.CreateNoteRequest;
import com.cloud.notesbackend.requests.UpdateNoteRequest;
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

    public BasicResponse updateNote(UpdateNoteRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);

        Note note = noteRepository.findNoteByIdAndUser(request.getId(), user);

        if (note == null) {
            throw new BadRequestException("Note not found for this user");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        noteRepository.save(note);

        return new BasicResponse(true, "Note updated successfully");
    }

    public BasicResponse deleteNote(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);

        Note note = noteRepository.findNoteByIdAndUser(id, user);

        if (note == null) {
            throw new BadRequestException("Note not found for this user");
        }

        noteRepository.delete(note);

        return new BasicResponse(true, "Note deleted successfully");
    }
}

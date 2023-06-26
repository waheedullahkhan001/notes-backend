package com.cloud.notesbackend.services;

import com.cloud.notesbackend.entities.Note;
import com.cloud.notesbackend.exceptions.NotFoundException;
import com.cloud.notesbackend.repositories.NoteRepository;
import com.cloud.notesbackend.repositories.UserRepository;
import com.cloud.notesbackend.requests.CreateNoteRequest;
import com.cloud.notesbackend.requests.UpdateNoteRequest;
import com.cloud.notesbackend.responses.BasicResponse;
import com.cloud.notesbackend.responses.GetAllNotesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("user");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createNote_NoteCreated() {
        Mockito.when(
                userRepository.findUserByUsername(Mockito.anyString())
        ).thenReturn(
                null
        );

        Mockito.when(
                noteRepository.save(Mockito.any())
        ).thenReturn(
                null
        );

        CreateNoteRequest request = new CreateNoteRequest();
        request.setTitle("Title");
        request.setContent("Content");

        BasicResponse basicResponse = noteService.createNote(request);

        Assertions.assertTrue(basicResponse.getSuccess());
        Assertions.assertEquals("Note created successfully", basicResponse.getMessage());
    }

    @Test
    void getAllNotes_NotesReturned() {
        Mockito.when(
                noteRepository.findAllByUserUsername(Mockito.anyString())
        ).thenReturn(
                List.of(new Note())
        );

        GetAllNotesResponse response = noteService.getAllNotes();

        Assertions.assertNotNull(response.getNotes());
    }

    @Test
    void getAllNotes_NotesNull() {
        Mockito.when(
                noteRepository.findAllByUserUsername(Mockito.anyString())
        ).thenReturn(
                null
        );

        GetAllNotesResponse response = noteService.getAllNotes();

        Assertions.assertNull(response.getNotes());
    }

    @Test
    void updateNote_NoteNotFound() {
        Mockito.when(
                noteRepository.findNoteByIdAndUserUsername(Mockito.anyLong(), Mockito.anyString())
        ).thenReturn(
                null
        );

        UpdateNoteRequest request = new UpdateNoteRequest();
        request.setTitle("Title");
        request.setContent("Content");

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                noteService.updateNote(1L, request));
        Assertions.assertEquals("Note not found for this user", exception.getMessage());
    }

    @Test
    void updateNote_NoteUpdated() {
        Mockito.when(
                noteRepository.findNoteByIdAndUserUsername(Mockito.anyLong(), Mockito.anyString())
        ).thenReturn(
                new Note()
        );

        Mockito.when(
                noteRepository.save(Mockito.any())
        ).thenReturn(
                new Note()
        );

        UpdateNoteRequest request = new UpdateNoteRequest();
        request.setTitle("Title");
        request.setContent("Content");

        BasicResponse basicResponse = noteService.updateNote(1L, request);

        Assertions.assertTrue(basicResponse.getSuccess());
        Assertions.assertEquals("Note updated successfully", basicResponse.getMessage());
    }

    @Test
    void deleteNote_NoteNotFound() {
        Mockito.when(
                noteRepository.findNoteByIdAndUserUsername(Mockito.anyLong(), Mockito.anyString())
        ).thenReturn(
                null
        );

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                noteService.deleteNote(1L));
        Assertions.assertEquals("Note not found for this user", exception.getMessage());
    }

    @Test
    void deleteNote_NoteDeleted() {
        Mockito.when(
                noteRepository.findNoteByIdAndUserUsername(Mockito.anyLong(), Mockito.anyString())
        ).thenReturn(
                new Note()
        );

        Mockito.doNothing().when(noteRepository).delete(Mockito.any());

        BasicResponse basicResponse = noteService.deleteNote(1L);

        Assertions.assertTrue(basicResponse.getSuccess());
        Assertions.assertEquals("Note deleted successfully", basicResponse.getMessage());
    }
}
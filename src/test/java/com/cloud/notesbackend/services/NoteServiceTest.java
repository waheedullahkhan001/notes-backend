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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration
class NoteServiceTest {


    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
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
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    }

    @Test
    @WithMockUser
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
    }
}
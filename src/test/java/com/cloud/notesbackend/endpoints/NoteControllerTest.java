package com.cloud.notesbackend.endpoints;


import com.cloud.notesbackend.entities.User;
import com.cloud.notesbackend.repositories.NoteRepository;
import com.cloud.notesbackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NoteRepository noteRepository;


    @Test
    void createNote_success() {
        Mockito.when(userRepository.findUserByUsername(Mockito.any())).thenReturn(new User());
        Mockito.when(noteRepository.save(Mockito.any())).thenReturn(null);

        // TODO: :)
    }
}

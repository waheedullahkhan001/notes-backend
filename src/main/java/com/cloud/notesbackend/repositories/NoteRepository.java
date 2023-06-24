package com.cloud.notesbackend.repositories;

import com.cloud.notesbackend.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserUsername(String username);

    Note findNoteByIdAndUserUsername(Long id, String username);
}

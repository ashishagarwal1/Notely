package com.example.ashishaggarwal.notely.interfaces;

import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;

public interface NotesUpdatesListener {

    void updateNote(NotesDataEntity notesDataEntity, boolean refreshRequired);

    void insertNote(NotesDataEntity notesDataEntity);

    void deleteNote(NotesDataEntity notesDataEntity);
}

package com.example.ashishaggarwal.notely.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.ashishaggarwal.notely.room.DbConstants;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;

import java.util.Collection;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by ashishaggarwal on 29/01/18.
 */

@Dao
public interface NotesDataDao {

    @Insert(onConflict = REPLACE)
    void insertNotesData(NotesDataEntity notesDataEntity);

    @Query("SELECT * FROM " + DbConstants.NOTES_DATA_TABLE + " ORDER BY "+ DbConstants.NotesData.LATEST_TIMESTAMP + " DESC")
    List<NotesDataEntity> getNotesData();

    @Query("DELETE FROM "+ DbConstants.NOTES_DATA_TABLE + " WHERE " + DbConstants.NotesData.CREATED_TIMESTAMP + " = :createdTs ")
    int deleteNoteData(long createdTs);


}

package com.example.ashishaggarwal.notely.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.ashishaggarwal.notely.room.dao.NotesDataDao;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;

import javax.inject.Singleton;

/**
 * Created by ashishaggarwal on 29/01/18.
 */

@Database(entities = {NotesDataEntity.class}, version = 1, exportSchema = false)
@Singleton
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;

    public abstract NotesDataDao notesDataDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (database == null) {
            database = Room
                    .databaseBuilder(context.getApplicationContext()
                            , AppDatabase.class, "user-database")
                    .build();
        }
        return database;
    }
}
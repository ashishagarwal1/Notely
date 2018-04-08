package com.example.ashishaggarwal.notely.injection.component;

import android.app.Application;
import android.content.Context;

import com.example.ashishaggarwal.notely.viewmodel.NotesViewModel;
import com.example.ashishaggarwal.notely.data.NotesDataRepository;
import com.example.ashishaggarwal.notely.injection.ApplicationContext;
import com.example.ashishaggarwal.notely.injection.module.ApplicationModule;
import com.example.ashishaggarwal.notely.room.db.AppDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    AppDatabase getDatabase();

    NotesDataRepository getNotesDataRepository();

    void inject(NotesViewModel notesViewModel);

    void inject(NotesDataRepository notesDataRepository);
}

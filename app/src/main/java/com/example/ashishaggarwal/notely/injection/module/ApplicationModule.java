package com.example.ashishaggarwal.notely.injection.module;

import android.app.Application;
import android.content.Context;

import com.example.ashishaggarwal.notely.data.NotesDataRepository;
import com.example.ashishaggarwal.notely.injection.ApplicationContext;
import com.example.ashishaggarwal.notely.room.db.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    AppDatabase provideDatabase() {
        return AppDatabase.getAppDatabase(mApplication);
    }

    @Provides
    @Singleton
    NotesDataRepository provideNotesDataRepository() {
        return new NotesDataRepository();
    }



}

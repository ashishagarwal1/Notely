package com.example.ashishaggarwal.notely.data;

import com.example.ashishaggarwal.notely.NotelyApplication;
import com.example.ashishaggarwal.notely.room.db.AppDatabase;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class NotesDataRepository {

    @Inject
    AppDatabase database;

    public NotesDataRepository() {
        NotelyApplication.getInstance().getComponent().inject(this);
    }

    public Observable<List<NotesDataEntity>> getNotesData() {
        return Observable.create(new ObservableOnSubscribe<List<NotesDataEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NotesDataEntity>> e) throws Exception {
                e.onNext(database.notesDataDao().getNotesData());
            }
        }).subscribeOn(Schedulers.io());
    }

    public void deleteNoteData(final NotesDataEntity notesDataEntity) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                database.notesDataDao().deleteNoteData(notesDataEntity.getCreatedTimeStamp());
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void insertNoteData(final NotesDataEntity notesDataEntity) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                database.notesDataDao().insertNotesData(notesDataEntity);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}

package com.example.ashishaggarwal.notely.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ashishaggarwal.notely.NotelyApplication;
import com.example.ashishaggarwal.notely.R;
import com.example.ashishaggarwal.notely.interfaces.NotesUpdatesListener;
import com.example.ashishaggarwal.notely.data.NotesDataRepository;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class NotesViewModel extends ViewModel implements NotesUpdatesListener {

    @Inject
    NotesDataRepository notesDataRepository;

    private final DisposableObserver disposableObserver;

    private MutableLiveData<List<NotesDataEntity>> liveData = new MutableLiveData<List<NotesDataEntity>>();

    private List<NotesDataEntity> notesDataEntityList = new ArrayList<>();

    private List<Integer> appliedFilterList = new ArrayList<>();

    public NotesViewModel() {
        NotelyApplication.getInstance().getComponent().inject(this);
        disposableObserver = notesDataRepository
                .getNotesData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<NotesDataEntity>>() {

                    @Override
                    public void onNext(List<NotesDataEntity> o) {
                        notesDataEntityList = o;
                        liveDataSetValue();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposableObserver != null)
            disposableObserver.dispose();
    }

    public LiveData<List<NotesDataEntity>> getNotesLiveData() {
        return liveData;
    }

    @Override
    public void updateNote(NotesDataEntity notesDataEntity, boolean refreshRequired) {
        if (refreshRequired) {
            Iterator<NotesDataEntity> iterator = notesDataEntityList.iterator();
            while (iterator.hasNext()) {
                NotesDataEntity notesData = iterator.next();
                if (notesData.getCreatedTimeStamp() == notesDataEntity.getCreatedTimeStamp()) {
                    iterator.remove();
                    break;
                }
            }
            notesDataEntityList.add(0, notesDataEntity);
            liveDataSetValue();
        }
        notesDataRepository.insertNoteData(notesDataEntity);
    }

    @Override
    public void insertNote(NotesDataEntity notesDataEntity) {
        notesDataEntityList.add(0, notesDataEntity);
        liveDataSetValue();
        notesDataRepository.insertNoteData(notesDataEntity);
    }

    private void liveDataSetValue() {
        List<NotesDataEntity> list = new ArrayList<>(notesDataEntityList);
        boolean isHearted = false, isFavourite = false;
        if (appliedFilterList.contains(R.string.hearted)) {
            isHearted = true;
        }
        if (appliedFilterList.contains(R.string.favourite)) {
            isFavourite = true;
        }
        Iterator<NotesDataEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            NotesDataEntity notesDataEntity = iterator.next();
            if (isHearted && !notesDataEntity.isLoved()) {
                iterator.remove();
                continue;
            }
            if (isFavourite && !notesDataEntity.isStarred()) {
                iterator.remove();
                continue;
            }
        }
        liveData.setValue(list);
    }

    @Override
    public void deleteNote(NotesDataEntity notesDataEntity) {
        notesDataEntityList.remove(notesDataEntity);
        liveDataSetValue();
        notesDataRepository.deleteNoteData(notesDataEntity);
    }

    public void setAppliedFilterList(List<Integer> appliedFilterList) {
        this.appliedFilterList = appliedFilterList;
        liveDataSetValue();
    }

    public List<Integer> getAppliedFilterList() {
        return appliedFilterList;
    }

    public NotesDataEntity getNotesItem(long createdTimestamp) {
        if (createdTimestamp == 0)
            return new NotesDataEntity();
        for (NotesDataEntity entity : notesDataEntityList) {
            if (entity.getCreatedTimeStamp() == createdTimestamp) {
                return entity;
            }
        }
        return new NotesDataEntity();
    }
}

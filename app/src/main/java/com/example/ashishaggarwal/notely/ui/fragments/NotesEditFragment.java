package com.example.ashishaggarwal.notely.ui.fragments;

import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ashishaggarwal.notely.R;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;
import com.example.ashishaggarwal.notely.utils.TextViewUndo;
import com.example.ashishaggarwal.notely.utils.Utils;
import com.example.ashishaggarwal.notely.viewmodel.NotesViewModel;

public class NotesEditFragment extends Fragment implements View.OnClickListener {

    private static final String NOTE_CREATED_TIMESTAMP = "noteCreatedTimeStamp";

    private static final String HISTORY_OBJECT = "historyObject";

    private NotesViewModel notesViewModel;

    private NotesDataEntity noteEntity;

    private EditText headerView;

    private EditText descView;

    private TextViewUndo textViewUndo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        notesViewModel =
                ViewModelProviders.of((FragmentActivity) getActivity()).get(NotesViewModel.class);

        View view = inflater.inflate(R.layout.fragment_edit_notes, null);
        initializeResources(view);
        createUndoTextInstance(savedInstanceState);
        return view;
    }

    private void createUndoTextInstance(final Bundle savedInstanceState) {
        textViewUndo = new TextViewUndo();
        Handler handler = new Handler();
        if (savedInstanceState != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (savedInstanceState == null)
                        return;
                    TextViewUndo.EditHistory editHistory = savedInstanceState.getParcelable(HISTORY_OBJECT);
                    if (editHistory == null)
                        return;
                    textViewUndo = new TextViewUndo();
                    textViewUndo.setmEditHistory(editHistory);
                }
            }, 300);
        }
    }

    private void initializeResources(View view) {
        long createdTimeStamp = getArguments().getLong(NOTE_CREATED_TIMESTAMP);
        noteEntity = notesViewModel.getNotesItem(createdTimeStamp);

        headerView = view.findViewById(R.id.header);
        descView = view.findViewById(R.id.desc);

        ((Toolbar) view.findViewById(R.id.my_awesome_toolbar)).setNavigationOnClickListener(nabigationBackClickListener);
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.undo).setOnClickListener(this);
        if (!TextUtils.isEmpty(noteEntity.getHeader())) {
            headerView.setText(noteEntity.getHeader());
        }
        if (!TextUtils.isEmpty(noteEntity.getDescription())) {
            descView.setText(noteEntity.getDescription());
        }
        headerView.addTextChangedListener(new CustomTextWatcher(headerView.getId()));
        descView.addTextChangedListener(new CustomTextWatcher(descView.getId()));
    }


    private class CustomTextWatcher implements TextWatcher {

        private int resId;

        private CustomTextWatcher(int resId) {
            this.resId = resId;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textViewUndo.beforeTextChanged(s, start, count);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textViewUndo.onTextChanged(s, start, count, resId);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public static NotesEditFragment newInstance(long createdTimeStamp) {
        NotesEditFragment fragment = new NotesEditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(NOTE_CREATED_TIMESTAMP, createdTimeStamp);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NotesEditFragment newInstance() {
        return newInstance(0);
    }

    private void saveClicked() {
        noteEntity.setDescription(descView.getText().toString());
        noteEntity.setHeader(headerView.getText().toString());
        noteEntity.setLatestTimeStamp(System.currentTimeMillis());
        if (noteEntity.getCreatedTimeStamp() == 0) {
            noteEntity.setCreatedTimeStamp(System.currentTimeMillis());
            notesViewModel.insertNote(noteEntity);
        } else {
            notesViewModel.updateNote(noteEntity, true);
        }
        onBackPressed();
    }


    private View.OnClickListener nabigationBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private void onBackPressed() {
        Utils.hideSoftKeyboard(getActivity());
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (textViewUndo != null && textViewUndo.getmEditHistory() != null) {
            outState.putParcelable(HISTORY_OBJECT, textViewUndo.getmEditHistory());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                saveClicked();
                break;
            case R.id.undo:
                if (textViewUndo.getPreviousResId() == -1) {
                    return;
                }
                if (textViewUndo.getPreviousResId() == headerView.getId())
                    textViewUndo.undo(headerView.getEditableText());
                if (textViewUndo.getPreviousResId() == descView.getId())
                    textViewUndo.undo(descView.getEditableText());
                break;
        }
    }
}

package com.example.ashishaggarwal.notely.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ashishaggarwal.notely.interfaces.NotesActivityListener;
import com.example.ashishaggarwal.notely.ui.fragments.NotesEditFragment;
import com.example.ashishaggarwal.notely.utils.Utils;
import com.example.ashishaggarwal.notely.viewmodel.NotesViewModel;
import com.example.ashishaggarwal.notely.R;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;
import com.example.ashishaggarwal.notely.ui.adapters.NotesDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener, NotesActivityListener {

    private final String NOTES_EDIT_FRAGMENT_TAG = "notesEditFragmentTag";

    private List<View> radioList;

    private RecyclerView notesRecyclerView;

    private NotesDataAdapter notesDataAdapter;

    private NotesViewModel notesViewModel;

    private DrawerLayout drawerLayout;

    private View filterCircleView;

    private View createNoteView;

    private View contentFrame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        notesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        initializeResources();
    }

    private void initializeResources() {
        drawerLayout = findViewById(R.id.drawer_layout);
        createNoteView = findViewById(R.id.create_note);
        notesRecyclerView = findViewById(R.id.notes_rv);
        contentFrame = findViewById(R.id.content_frame);
        filterCircleView = findViewById(R.id.filter_circle);
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            contentFrame.setVisibility(View.GONE);
        }
        setRadioGroup();
        createRecyclerView();
        placeListeners();
    }

    private void placeListeners() {

        findViewById(R.id.drawer_cross).setOnClickListener(this);
        findViewById(R.id.apply).setOnClickListener(this);
        findViewById(R.id.create_post).setOnClickListener(this);
        findViewById(R.id.filter_circle_fl).setOnClickListener(this);
        createNoteView.setOnClickListener(this);
        drawerLayout.addDrawerListener(drawerListener);
        notesViewModel.getNotesLiveData().observe(this, onDataChange());
    }

    private void createRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(notesRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        notesDataAdapter = new NotesDataAdapter(notesViewModel, this);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.addItemDecoration(dividerItemDecoration);
        notesRecyclerView.setAdapter(notesDataAdapter);
    }

    private Observer<List<NotesDataEntity>> onDataChange() {
        return new Observer<List<NotesDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<NotesDataEntity> o) {
                createNoteView.setVisibility(o.size() > 0 ? View.GONE : View.VISIBLE);
                notesDataAdapter.setNotesDataEntities(o);
            }
        };
    }

    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            List<Integer> list = new ArrayList<>();
            for (View view : radioList) {
                if (view.isSelected()) {
                    list.add((Integer) view.getTag());
                }
            }
            filterCircleView.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
            notesViewModel.setAppliedFilterList(list);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private void setRadioGroup() {
        radioList = new ArrayList<>();
        radioList.add(createRadioView(R.string.hearted));
        radioList.add(createRadioView(R.string.favourite));
        radioList.add(createRadioView(R.string.poem));
        radioList.add(createRadioView(R.string.story));
        LinearLayout radioGroupView = findViewById(R.id.radio_group_navbar);
        for (View view : radioList) {
            radioGroupView.addView(view);
        }
        List<Integer> list = notesViewModel.getAppliedFilterList();
        for (View view : radioList) {
            if (list.contains(view.getTag())) {
                view.setSelected(true);
            }
        }
        filterCircleView.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
    }

    private View createRadioView(int resId) {
        View item = getLayoutInflater().inflate(R.layout.radio_item, null);
        TextView tv = item.findViewById(R.id.radio_tv);
        tv.setText(getString(resId));
        item.setOnClickListener(radioOnClickListener);
        item.setTag(resId);
        return item;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private View.OnClickListener radioOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < radioList.size(); i++) {
                if (radioList.get(i).getTag().equals(v.getTag())) {
                    if (radioList.get(i).isSelected()) {
                        radioList.get(i).setSelected(false);
                    } else {
                        radioList.get(i).setSelected(true);
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.apply:
            case R.id.drawer_cross:
                drawerLayout.closeDrawers();
                break;
            case R.id.create_post:
            case R.id.create_note:
                openFragment(0);
                break;
            case R.id.filter_circle_fl:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    @Override
    public void openFragment(long createdTs) {
        contentFrame.setVisibility(View.GONE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Fragment fragment = NotesEditFragment.newInstance(createdTs);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(NOTES_EDIT_FRAGMENT_TAG);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
            contentFrame.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            Utils.hideSoftKeyboard(this);
        } else super.onBackPressed();
    }
}


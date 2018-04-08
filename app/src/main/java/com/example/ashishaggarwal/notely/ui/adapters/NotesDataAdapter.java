package com.example.ashishaggarwal.notely.ui.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.ashishaggarwal.notely.R;
import com.example.ashishaggarwal.notely.interfaces.NotesActivityListener;
import com.example.ashishaggarwal.notely.interfaces.NotesUpdatesListener;
import com.example.ashishaggarwal.notely.room.entities.NotesDataEntity;

import java.util.ArrayList;
import java.util.List;

import static android.text.format.DateUtils.FORMAT_SHOW_TIME;
import static android.text.format.DateUtils.SECOND_IN_MILLIS;
import static android.text.format.DateUtils.WEEK_IN_MILLIS;

public class NotesDataAdapter extends RecyclerView.Adapter<NotesDataAdapter.NotesDataViewHolder> {

    private final NotesUpdatesListener notesUpdatesListener;

    private final NotesActivityListener activityListener;

    private List<NotesDataEntity> notesDataEntities;

    private boolean isSwipe;

    public NotesDataAdapter(NotesUpdatesListener notesUpdatesListener, NotesActivityListener activityListener) {
        notesDataEntities = new ArrayList<>();
        this.notesUpdatesListener = notesUpdatesListener;
        this.activityListener = activityListener;
    }

    public void setNotesDataEntities(List<NotesDataEntity> notesDataEntities) {
        this.notesDataEntities = notesDataEntities;
        notifyDataSetChanged();
    }

    public List<NotesDataEntity> getNotesDataEntities() {
        return notesDataEntities;
    }

    @Override
    public NotesDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item, parent, false);
        return new NotesDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotesDataViewHolder holder, int position) {
        NotesDataEntity notesDataEntity = notesDataEntities.get(position);
        holder.header.setText(TextUtils.isEmpty(notesDataEntity.getHeader()) ? holder.header.getContext().getString(R.string.empty) : notesDataEntity.getHeader());
        holder.description.setText(TextUtils.isEmpty(notesDataEntity.getDescription()) ? holder.description.getContext().getString(R.string.empty) : notesDataEntity.getDescription());
        holder.timestamp.setText(DateUtils.getRelativeDateTimeString(holder.timestamp.getContext(), notesDataEntity.getLatestTimeStamp(), SECOND_IN_MILLIS, WEEK_IN_MILLIS, FORMAT_SHOW_TIME));
        holder.swipeLayout.setTag(notesDataEntity);
        holder.loved.setTag(notesDataEntity);
        holder.favourite.setTag(notesDataEntity);
        holder.loved.setSelected(notesDataEntity.isLoved());
        holder.favourite.setSelected(notesDataEntity.isStarred());
        holder.itemView.setTag(notesDataEntity);
    }

    @Override
    public int getItemCount() {
        return notesDataEntities.size();
    }

    public class NotesDataViewHolder extends RecyclerView.ViewHolder {

        private final TextView header, description, timestamp;

        private final SwipeLayout swipeLayout;

        private final View loved, favourite;

        public NotesDataViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            description = itemView.findViewById(R.id.desc);
            timestamp = itemView.findViewById(R.id.timestamp);
            loved = itemView.findViewById(R.id.loved);
            favourite = itemView.findViewById(R.id.favourite);
            loved.setOnClickListener(clickListener);
            favourite.setOnClickListener(clickListener);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_wrapper));
            swipeLayout.addSwipeListener(swipeListener);
            swipeLayout.setOnClickListener(clickListener);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loved:
                    NotesDataEntity dataEntity = (NotesDataEntity) v.getTag();
                    dataEntity.setLoved(!dataEntity.isLoved());
                    v.setSelected(dataEntity.isLoved());
                    notesUpdatesListener.updateNote(dataEntity, false);
                    break;
                case R.id.favourite:
                    NotesDataEntity entity = (NotesDataEntity) v.getTag();
                    entity.setStarred(!entity.isStarred());
                    v.setSelected(entity.isStarred());
                    notesUpdatesListener.updateNote(entity, false);
                    break;
                case R.id.swipe_layout:
                    if (!isSwipe) {
                        activityListener.openFragment(((NotesDataEntity) v.getTag()).getCreatedTimeStamp());
                    }
                    isSwipe = false;
                    break;
            }
        }
    };

    private Handler handler = new Handler();

    private EntityDeleteRunnable runnable = new EntityDeleteRunnable();

    private SwipeLayout.SwipeListener swipeListener = new SwipeLayout.SwipeListener() {

        @Override
        public void onStartOpen(SwipeLayout layout) {
            isSwipe = true;
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            runnable.setEntity((NotesDataEntity) layout.getTag());
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 300);
        }

        @Override
        public void onStartClose(SwipeLayout layout) {
        }

        @Override
        public void onClose(SwipeLayout layout) {
        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
        }
    };

    private class EntityDeleteRunnable implements Runnable {

        private NotesDataEntity entity;

        public void setEntity(NotesDataEntity entity) {
            this.entity = entity;
        }

        @Override
        public void run() {
            notesUpdatesListener.deleteNote(entity);
        }
    }
}

package com.example.ashishaggarwal.notely.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.ashishaggarwal.notely.room.DbConstants;

/**
 * Created by ashishaggarwal on 29/01/18.
 */

@Entity(tableName = DbConstants.NOTES_DATA_TABLE)
public class NotesDataEntity {

    @ColumnInfo(name = DbConstants.NotesData.HEADER)
    private String header;

    @ColumnInfo(name = DbConstants.NotesData.DESCRIPTION)
    private String description;

    @ColumnInfo(name = DbConstants.NotesData.LATEST_TIMESTAMP)
    private long latestTimeStamp;

    @ColumnInfo(name = DbConstants.NotesData.CREATED_TIMESTAMP)
    @PrimaryKey
    private long createdTimeStamp;

    @ColumnInfo(name = DbConstants.NotesData.STARRED)
    private boolean starred;

    @ColumnInfo(name = DbConstants.NotesData.LOVED)
    private boolean loved;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLatestTimeStamp() {
        return latestTimeStamp;
    }

    public void setLatestTimeStamp(long latestTimeStamp) {
        this.latestTimeStamp = latestTimeStamp;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isLoved() {
        return loved;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }
}

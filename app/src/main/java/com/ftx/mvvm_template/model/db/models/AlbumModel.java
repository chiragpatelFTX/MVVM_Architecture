package com.ftx.mvvm_template.model.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Name : AlbumModel
 * <br> Purpose : GreenDAO database model of album table.
 */


@Entity(tableName = "albumsTbl")
public class AlbumModel {

    @PrimaryKey
    @JsonProperty("id")
    @ColumnInfo(name = "id")
    public int id;

    @JsonProperty("userId")
    @ColumnInfo(name = "userId")
    public int userId;

    @JsonProperty("title")
    @ColumnInfo(name = "title")
    public String title;

    @Ignore
    public AlbumModel() {
    }

    public AlbumModel(int id, int userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

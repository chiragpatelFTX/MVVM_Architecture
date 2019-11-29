package com.sql.projecttemplate.model.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Name : UserModel
 * <br> Purpose : ROOM database model of user table.
 */

@Entity(tableName = "UserTbl")
public class UserModel {

    @JsonProperty("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @JsonProperty("first_name")
    @ColumnInfo(name = "first_name")
    public String firstName;

    @JsonProperty("last_name")
    @ColumnInfo(name = "last_name")
    public String lastName;

    @JsonProperty("avatar")
    @ColumnInfo(name = "avatar")
    public String avatar;

    @Ignore
    public UserModel() {
    }

    public UserModel( String firstName, String lastName, String avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

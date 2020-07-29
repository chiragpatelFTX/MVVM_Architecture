package com.ftx.mvvm_template.model.db.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    /**
     * Name : DIFF_CALLBACK
     * <br> Purpose : DiffCallback will check weather your data is changed or not if yes than return true other wise return
     * false and according to this your adapter will update your record or insert new record in recycler view.
     */
    public static final DiffUtil.ItemCallback<UserModel> USER_DIFF_CALLBACK = new DiffUtil.ItemCallback<UserModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserModel oldUserModel, @NonNull UserModel newUserModel) {
            return oldUserModel.id == newUserModel.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserModel oldUserModel, @NonNull UserModel newUserModel) {
            return oldUserModel.equals(newUserModel);
        }
    };

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

    public UserModel(String firstName, String lastName, String avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }
}

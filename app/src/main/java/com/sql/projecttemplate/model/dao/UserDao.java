package com.sql.projecttemplate.model.dao;

import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sql.projecttemplate.model.db.models.UserModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


/**
 * Name : UserDao
 *<br> Purpose : Dao interface will contains db operation methods with appropriate annotations for UserModel.
 */

@Dao
public interface UserDao {

    //LivePagedListProvider used for observe LiveData<List<UserModel>> with Paging.
    @Query("Select * from UserTbl ORDER BY id ASC")
    LivePagedListProvider<Integer, UserModel> getPagedUserList();

    //Get userModel Data from id
    @Query("Select * from UserTbl where id = :userId")
    UserModel getUser(int userId);

    //Insert userModel Data
    @Insert(onConflict = REPLACE)
    void insertUser(UserModel userModel);

    //Insert list of userModel Data
    @Insert(onConflict = REPLACE)
    void insertUserList(List<UserModel> userModel);

    //Update userModel Data
    @Update
    void updateUser(UserModel userModel);

    //Delete Userentity data
    @Delete
    void deleteUser(UserModel userModel);

}

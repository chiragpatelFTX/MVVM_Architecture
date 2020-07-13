package com.ftx.mvvm_template.model.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ftx.mvvm_template.model.db.models.UserModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


/**
 * Name : UserDao
 * <br> Purpose : Dao interface will contains db operation methods with appropriate annotations for UserModel.
 */

@Dao
public interface UserDao {

    //LivePagedListProvider used for observe LiveData<List<UserModel>> with Paging.
    @Query("Select * from UserTbl ORDER BY id ASC")
    DataSource.Factory<Integer, UserModel> getPagedUserList();

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

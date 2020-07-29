package com.ftx.mvvm_template.model.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ftx.mvvm_template.model.db.models.AlbumModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Name : AlbumDao
 * <br> Purpose : Dao interface will contains db operation methods with appropriate annotations for AlbumModel.
 */

@Dao
public interface AlbumDao {

    //To get single AlbumModel from AlbumId
    @Query("Select * from albumsTbl where id = :albumId")
    AlbumModel getAlbum(int albumId);

    //LivePagedListProvider used for observe LiveData<List<AlbumModel>> with Paging.
    @Query("Select * from albumsTbl ORDER BY id ASC")
    DataSource.Factory<Integer, AlbumModel> getPagedAlbumList();

    //Insert AlbumModel Data
    @Insert(onConflict = REPLACE)
    void insertAlbum(AlbumModel albumModel);

    //Insert list of AlbumModel Data
    @Insert(onConflict = REPLACE)
    void insertAlbumList(List<AlbumModel> albumModelList);

    //Update AlbumModel Data
    @Update
    void updateAlbum(AlbumModel userModel);

    //Delete AlbumModel data
    @Delete
    void deleteAlbum(AlbumModel userModel);

}

package com.ftx.mvvm_template.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.ftx.mvvm_template.framework.model.ApiResponse;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.views.listeners.AsyncTaskCompleteListener;

import java.util.List;

/**
 * Name : Async
 * <br> Purpose : Generic AsyncTask for database operation.
 *
 * @param <T> : generic type object
 */

public class DatabaseAsync<T extends Object> extends AsyncTask<Void, Void, T> {

    T response;
    private final int caseInsertUserList;
    private final Context mContext;
    private final MyDatabase mDatabase;
    private final Object mObject;
    private final AsyncTaskCompleteListener mListener;


    public DatabaseAsync(Context context,
                         MyDatabase database,
                         Object obj,
                         int type,
                         AsyncTaskCompleteListener listener) {
        this.mContext = context;
        this.mDatabase = database;
        this.mObject = obj;
        this.caseInsertUserList = type;
        this.mListener = listener;
    }

    @Override
    protected T doInBackground(Void... voids) {

        switch (caseInsertUserList) {
            case Constants.Database.INSERT_USERLIST:
                mDatabase.userDao().insertUserList((List<UserModel>) ((ApiResponse) mObject).getListData());
                break;
            case Constants.Database.INSERT_ALBUMLIST:
                mDatabase.albumDao().insertAlbumList((List<AlbumModel>) ((ApiResponse) mObject).getListData());
                break;
            case Constants.Database.GET_ALBUM:
                response = (T) mDatabase.albumDao().getAlbum((Integer) mObject);
                break;
            case Constants.Database.GET_USER:
                response = (T) mDatabase.userDao().getUser((Integer) mObject);
                break;
            case Constants.Database.UPDATE_ALBUM:
                mDatabase.albumDao().updateAlbum((AlbumModel) mObject);
                break;
            case Constants.Database.DELETE_ALBUM:
                mDatabase.albumDao().deleteAlbum((AlbumModel) mObject);
                break;
            case Constants.Database.UPDATE_USER:
                mDatabase.userDao().updateUser((UserModel) mObject);
                break;
            case Constants.Database.DELETE_USER:
                mDatabase.userDao().deleteUser((UserModel) mObject);
                break;
        }

        return response;
    }

    @Override
    protected void onPostExecute(T object) {
        super.onPostExecute(object);

        mListener.onTaskComplete(object);

    }
}

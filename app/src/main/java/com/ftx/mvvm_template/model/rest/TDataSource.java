package com.ftx.mvvm_template.model.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.model.entities.response.BaseResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * Name : TDataSource
 * <br> Purpose : For get user data with pagination.
 */

public class TDataSource extends PageKeyedDataSource<Integer, UserModel> {

    private final MyDatabase mDatabase;
    private RestApis mApiService;
    //we will start from the first page which is 1
    private static final int FIRST_PAGE = 1;
    //the size of a page that we want
    public int LAST_PAGE = 10;

    public TDataSource() {
        mApiService = ServiceGenerator.createService(RestApis.class);
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, UserModel> callback) {
        try {
            Response<BaseResponse<List<UserModel>>> response = mApiService.getUserList(FIRST_PAGE).execute();
            if (response.isSuccessful() && response.code() == 200) {
                if (response.body() != null) {
                    callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);
                    mDatabase.userDao().insertUserList(response.body().getData());
                }
            } else {
                Log.e("API CALL", response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UserModel> callback) {
        try {
            Response<BaseResponse<List<UserModel>>> response = mApiService.getUserList(params.key).execute();
            if (response.isSuccessful() && response.code() == 200) {
                //if the current page is greater than one
                //we are decrementing the page number
                //else there is no previous page
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {

                    //passing the loaded data
                    //and the previous page key
                    callback.onResult(response.body().getData(), adjacentKey);
                }
            } else {
                Log.e("API CALL", response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UserModel> callback) {
        try {
            Response<BaseResponse<List<UserModel>>> response = mApiService.getUserList(params.key).execute();
            if (response.isSuccessful() && response.code() == 200) {
                if (response.body() != null) {
                    //if the response has next page
                    //incrementing the next page number
                    //TODO : check weather there is any page after this page
                    Integer key = LAST_PAGE == params.key ? params.key + 1 : null;

                    //passing the loaded data and next page value
                    callback.onResult(response.body().getData(), key);
                }
            } else {
                Log.e("API CALL", response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

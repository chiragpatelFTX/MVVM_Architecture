package com.ftx.mvvm_template.model.rest;

import android.arch.paging.DataSource;
import android.arch.paging.TiledDataSource;
import android.util.Log;

import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.model.entities.response.BaseResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Name : TDataSource
 * <br> Purpose : For get user data with pagination.
 */

public class TDataSource extends TiledDataSource<UserModel> {

    private final MyDatabase mDatabase;
    private RestApis mApiService;
    private int pageNumber = 0;

    public TDataSource() {
        mApiService = ServiceGenerator.createService(RestApis.class);
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();
    }

    @Override
    public int countItems() {
        return DataSource.COUNT_UNDEFINED;
    }

    @Override
    public List<UserModel> loadRange(int startPosition, int count) {
        List<UserModel> userModelList = new ArrayList();
        try {
            pageNumber += 1;
            Response<BaseResponse<List<UserModel>>> response = mApiService.getUserList(pageNumber).execute();
            if (response.isSuccessful() && response.code() == 200) {
                mDatabase.userDao().insertUserList(response.body().getData());
                userModelList.addAll(response.body().getData());
            } else {
                Log.e("API CALL", response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userModelList;
    }
}

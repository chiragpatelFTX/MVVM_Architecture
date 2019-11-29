package com.sql.projecttemplate.model.repo;

import android.arch.lifecycle.LiveData;

import com.sql.projecttemplate.framework.model.ApiResponse;
import com.sql.projecttemplate.model.entities.request.LoginRequest;
import com.sql.projecttemplate.model.entities.request.RegisterRequest;

/**
 * Name : HomeRepository
 *<br> Purpose : Repository for abstract the communication of rest of the code to the Data sources such as Database or API calls.
 */

public interface HomeRepository extends BaseRepository {
    LiveData<ApiResponse> loginUserOnServer(LoginRequest aRequest);
    LiveData<ApiResponse> registerUserOnServer(RegisterRequest aRequest);
    LiveData<ApiResponse> getAlbumList();
    LiveData<ApiResponse> getUserList(int pageNumber);
}
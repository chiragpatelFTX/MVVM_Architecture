package com.ftx.mvvm_template.model.rest;

import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.model.entities.request.LoginRequest;
import com.ftx.mvvm_template.model.entities.request.RegisterRequest;
import com.ftx.mvvm_template.model.entities.response.BaseResponse;
import com.ftx.mvvm_template.model.entities.response.LoginResponse;
import com.ftx.mvvm_template.model.entities.response.RegisterResponse;
import com.ftx.mvvm_template.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Name : RestApis
 *<br> Purpose :Contains web services use in application
 */
public interface RestApis {

    @POST(Constants.ApiMethods.REGISTER_URL)
    Call<RegisterResponse> registerUser(@Body RegisterRequest aRegisterRequest);

    @POST(Constants.ApiMethods.LOGIN_URL)
    Call<LoginResponse> loginUser(@Body LoginRequest aRequest);

    @GET(Constants.ApiMethods.USER_LIST_URL)
    Call<BaseResponse<List<UserModel>>> getUserList(@Query("page") int pageId);

    @GET(Constants.ApiMethods.ALBUM_LIST_URL)
    Call<List<AlbumModel>> getAlbumList();

}

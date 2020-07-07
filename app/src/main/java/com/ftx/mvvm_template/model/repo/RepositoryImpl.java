package com.ftx.mvvm_template.model.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.framework.model.ApiResponse;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.model.entities.request.LoginRequest;
import com.ftx.mvvm_template.model.entities.request.RegisterRequest;
import com.ftx.mvvm_template.model.entities.response.BaseResponse;
import com.ftx.mvvm_template.model.entities.response.LoginResponse;
import com.ftx.mvvm_template.model.entities.response.RegisterResponse;
import com.ftx.mvvm_template.model.rest.RestApis;
import com.ftx.mvvm_template.model.rest.ServiceGenerator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryImpl implements HomeRepository {

    private static RepositoryImpl newsRepository;

    public static RepositoryImpl getInstance() {
        if (newsRepository == null) {
            newsRepository = new RepositoryImpl();
        }
        return newsRepository;
    }

    private RestApis mApiService;

    public RepositoryImpl() {
        mApiService = ServiceGenerator.createService(RestApis.class);
    }

    /**
     * Name : loginUserOnServer
     * <br> Purpose : Login user on server.
     *
     * @param aRequest : request object of login
     * @return LiveData<ApiResponse>  : will return liveData of ApiResponse
     */

    public MutableLiveData<ApiResponse> loginUserOnServer(LoginRequest aRequest) {

        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();

        mApiService.loginUser(aRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse mResponse = response.body();
                    mResponse.setToken("abcdefghijklmnopqrstuvwxyz");
                    liveData.setValue(new ApiResponse(mResponse));
                } else {
                    try {
                        APIError mApiError = (APIError) TemplateApplication.getAppInstance().convertJsonToObject(response.errorBody().string(), APIError.class);
                        if (mApiError == null) {
                            mApiError = new APIError();
                            mApiError.setStatusMessage("There are some issue please verify.");
                        }

                        mApiError.setStatusCode(response.code());
                        liveData.setValue(mApiError);
                    } catch (IOException aE) {
                        aE.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new APIError(-1, t.getMessage()));
            }
        });

        return liveData;
    }

    /**
     * Name : getAlbumList
     * <br> Purpose : In the getAlbumList() method we create a MutableLiveData from the data obtained
     * from retrofit. MutableLiveData is the subclass of LiveData that has setValue(T) method
     * that can be used to modify the value it holds.
     */
    public LiveData<ApiResponse> getAlbumList() {
        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();
        mApiService.getAlbumList().enqueue(new Callback<List<AlbumModel>>() {
            @Override
            public void onResponse(Call<List<AlbumModel>> call, Response<List<AlbumModel>> response) {
                if (response.isSuccessful()) {
                    List<AlbumModel> albumList = response.body();
                    liveData.setValue(new ApiResponse(albumList));
                } else {
                    try {
                        APIError mApiError = (APIError) TemplateApplication.getAppInstance().convertJsonToObject(response.errorBody().string(), APIError.class);
                        if (mApiError == null) {
                            mApiError = new APIError();
                            mApiError.setStatusMessage("There are some issue please verify.");
                        }

                        mApiError.setStatusCode(response.code());
                        liveData.setValue(new ApiResponse(mApiError));
                    } catch (IOException aE) {
                        aE.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AlbumModel>> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ApiResponse(new APIError(-1, t.getMessage())));
            }

        });

        return liveData;
    }

    /**
     * Name : getUserList
     * <br> Purpose : fetch user list which are on our server.
     */
    public LiveData<ApiResponse> getUserList(int pageId) {

        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();

        mApiService.getUserList(pageId).enqueue(new Callback<BaseResponse<List<UserModel>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<UserModel>>> call, Response<BaseResponse<List<UserModel>>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> albumList = response.body().getData();
                    liveData.setValue(new ApiResponse(albumList));
                } else {
                    try {
                        APIError mApiError = (APIError) TemplateApplication.getAppInstance().convertJsonToObject(response.errorBody().string(), APIError.class);
                        if (mApiError == null) {
                            mApiError = new APIError();
                            mApiError.setStatusMessage("There are some issue please verify.");
                        }

                        mApiError.setStatusCode(response.code());
                        liveData.setValue(mApiError);
                    } catch (IOException aE) {
                        aE.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<UserModel>>> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new APIError(-1, t.getMessage()));
            }
        });

        return liveData;
    }

    /**
     * Name : registerUserOnServer
     * <br> Purpose : register user on server.
     *
     * @param aRequest : request object for registration
     */
    public LiveData<ApiResponse> registerUserOnServer(RegisterRequest aRequest) {

        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();

        mApiService.registerUser(aRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse mResponse = response.body();
                    mResponse.setToken("abcdefghijklmnopqrstuvwxyz");
                    liveData.setValue(new ApiResponse(mResponse));
                } else {
                    try {

                        APIError mApiError = (APIError) TemplateApplication.getAppInstance().
                                convertJsonToObject(response.errorBody().string(), APIError.class);
                        if (mApiError == null) {
                            mApiError = new APIError();
                            mApiError.setStatusMessage("There are some issue please verify.");
                        }

                        mApiError.setStatusCode(response.code());
                        liveData.setValue(mApiError);
                    } catch (IOException aE) {
                        aE.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue((new APIError(-1, t.getMessage())));
            }
        });

        return liveData;
    }

}
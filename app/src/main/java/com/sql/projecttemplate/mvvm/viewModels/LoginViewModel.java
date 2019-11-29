package com.sql.projecttemplate.mvvm.viewModels;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sql.projecttemplate.R;
import com.sql.projecttemplate.TemplateApplication;
import com.sql.projecttemplate.framework.model.APIError;
import com.sql.projecttemplate.framework.model.ApiResponse;
import com.sql.projecttemplate.model.db.MyDatabase;
import com.sql.projecttemplate.model.entities.request.LoginRequest;
import com.sql.projecttemplate.model.entities.response.LoginResponse;
import com.sql.projecttemplate.model.repo.HomeRepository;
import com.sql.projecttemplate.model.repo.RepositoryImpl;
import com.sql.projecttemplate.mvvm.views.LoginView;
import com.sql.projecttemplate.mvvm.views.MvvmView;
import com.sql.projecttemplate.utils.network.NetworkUtils;


/**
 * Name : LoginViewModel
 * <br> Purpose :  The LoginViewModel extends ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
 * The ViewModel class allows data to survive configuration changes such as screen rotations.
 */

public class LoginViewModel extends BaseViewModel {

    public static final String TAG = BaseViewModel.class.getName();
    public Context mContext;
    private MyDatabase mDatabase;
    private MediatorLiveData<ApiResponse> mApiResponse;
    private HomeRepository mHomeRepo;
    private LoginView mLoginView;

    // No argument constructor
    public LoginViewModel() {
    }

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        this.mContext = mContext;
        this.mLoginView = (LoginView) mvpView;
        mApiResponse = new MediatorLiveData<>();
        mHomeRepo = new RepositoryImpl();
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();

        return super.inIt(mContext, mvpView);
    }

    @NonNull
    public LiveData<ApiResponse> getApiResponse() {
        return mApiResponse;
    }

    public LiveData<ApiResponse> loadResponse(final String sEmail, final String sPassword) {

        LoginRequest mRequest = new LoginRequest();
        mRequest.setEmail(sEmail);
        mRequest.setPassword(sPassword);

        mApiResponse.addSource(
                mHomeRepo.loginUserOnServer(mRequest), apiResponse -> mApiResponse.setValue(apiResponse)
        );
        return mApiResponse;
    }


    public void loginUser(String sUserName, String sPassword) {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
            mLoginView.noInternetConnection(() -> loginUser(sUserName, sPassword));
        } else {
            mLoginView.showLoader(mContext.getString(R.string.message_loader_logging));
            loadResponse(sUserName, sPassword);
            getApiResponse().observe((LifecycleOwner) mContext, apiResponse -> {
                mLoginView.hideLoader();
                if (apiResponse != null) {
                    if (apiResponse.getSingalData() instanceof APIError) {
//                      HandleError
                        mLoginView.apiError((APIError) apiResponse.getSingalData());
                    } else {
                        LoginResponse mLoginResponse = apiResponse.getSingalData() instanceof LoginResponse ? (LoginResponse) apiResponse.getSingalData() : new LoginResponse();
                        if (mLoginResponse != null && !TextUtils.isEmpty(mLoginResponse.getToken())) {
                            Log.d(TAG, mLoginResponse.getToken());
                            mLoginView.onLoginSuccess(mLoginResponse);
                        }
                    }
                }
            });
        }
    }

}

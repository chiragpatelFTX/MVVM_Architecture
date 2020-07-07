package com.ftx.mvvm_template.mvvm.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.databinding.FragmentLoginBinding;
import com.ftx.mvvm_template.framework.model.ApiResponse;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.entities.request.LoginRequest;
import com.ftx.mvvm_template.model.repo.HomeRepository;
import com.ftx.mvvm_template.model.repo.RepositoryImpl;
import com.ftx.mvvm_template.mvvm.views.LoginView;
import com.ftx.mvvm_template.mvvm.views.MvvmView;
import com.ftx.mvvm_template.utils.StringUtils;
import com.ftx.mvvm_template.utils.network.NetworkUtils;


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

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        this.mContext = mContext;
        this.mLoginView = (LoginView) mvpView;
        mApiResponse = new MediatorLiveData<>();
        mHomeRepo = RepositoryImpl.getInstance();
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();
            return super.inIt(mContext, mvpView);
    }

    @NonNull
    public LiveData<ApiResponse> getApiResponse() {
        return mApiResponse;
    }

    public void onLoginClicked() {
        mLoginView.onLoginClicked();
    }

    public void onFacebookClicked() {
        mLoginView.onClickFacebookLogin();
    }

    public void onGmailClicked() {
        mLoginView.onClickGmailLogin();
    }

    public void onClickRegister() {
        mLoginView.onClickRegister();
    }

    /**
     * Name : LoginFragment validateForm
     * <br> Purpose :
     * This method will first locally validate the login form.
     * and if there are any local errors then we will set error to respected
     * view.
     *
     * @param mBinding
     */
    public void validateForm(FragmentLoginBinding mBinding) {
        if (StringUtils.isTrimmedEmpty(mBinding.aLoginEdtUsername.getText().toString())) {
            mBinding.inputLayoutUsername.setError(mContext.getString(R.string.error_enterEmailAddress));
        } else if (!StringUtils.isValidEmail(mBinding.aLoginEdtUsername.getText().toString())) {
            mBinding.inputLayoutUsername.setError(mContext.getString(R.string.error_enterValidEmailAddress));
        } else if (StringUtils.isTrimmedEmpty(mBinding.aLoginEdtPassword.getText().toString())) {
            mBinding.inputLayoutPassword.setError(mContext.getString(R.string.error_enterPassword));
        } else {
            loginUser(mBinding.aLoginEdtUsername.getText().toString(), mBinding.aLoginEdtPassword.getText().toString());
        }
    }

    public void loginUser(String sUserName, String sPassword) {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            mLoginView.toast("Please check internet connection");
            mLoginView.noInternetConnection(() -> loginUser(sUserName, sPassword));
        } else {
            mLoginView.showLoader(mContext.getString(R.string.message_loader_logging));
            mApiResponse.addSource(
                    mHomeRepo.loginUserOnServer(new LoginRequest(sUserName, sPassword)), apiResponse -> mApiResponse.setValue(apiResponse)
            );
        }
    }
}

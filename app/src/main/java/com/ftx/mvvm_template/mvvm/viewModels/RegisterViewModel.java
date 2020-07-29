package com.ftx.mvvm_template.mvvm.viewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.databinding.FragmentRegisterBinding;
import com.ftx.mvvm_template.framework.model.ApiResponse;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.entities.request.RegisterRequest;
import com.ftx.mvvm_template.model.repo.HomeRepository;
import com.ftx.mvvm_template.model.repo.RepositoryImpl;
import com.ftx.mvvm_template.mvvm.views.MvvmView;
import com.ftx.mvvm_template.mvvm.views.RegisterView;
import com.ftx.mvvm_template.utils.StringUtils;
import com.ftx.mvvm_template.utils.network.NetworkUtils;
import com.ftx.mvvm_template.views.fragments.RegisterFragment;

/**
 * Name : RegisterViewModel
 * <br> Purpose :  The RegisterViewModel extends ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
 * The ViewModel class allows data to survive configuration changes such as screen rotations.
 */

public class RegisterViewModel extends BaseViewModel {

    public static final String TAG = BaseViewModel.class.getName();

    public Context mContext;
    private MyDatabase mDatabase;
    private MediatorLiveData<ApiResponse> mRegLiveData;
    private HomeRepository mHomeRepo;
    private RegisterView mRegView;

    // No argument constructor
    public RegisterViewModel() {
    }

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        this.mContext = mContext;
        this.mRegView = (RegisterView) mvpView;
        mRegLiveData = new MediatorLiveData<>();
        mHomeRepo = new RepositoryImpl();
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();

        return super.inIt(mContext, mvpView);
    }

    @NonNull
    public LiveData<ApiResponse> regLiveData() {
        return mRegLiveData;
    }


    public void onClickRegister() {
        mRegView.onRegisterClicked();
    }

    /**
     * Name : RegisterFragment validateForm
     * <br> Purpose :
     * This method will validate the form locally.
     * and if any errors found then we will set errors to respected view.
     */
    public void validateForm(FragmentRegisterBinding mBinding) {
        if (StringUtils.isTrimmedEmpty(mBinding.fRegisterEdtEmail.getText().toString())) {
            mBinding.inputLayoutFirstname.setError("Please enter E-Mail Address");
        } else if (!StringUtils.isValidEmail(mBinding.fRegisterEdtEmail.getText().toString())) {
            mBinding.inputLayoutFirstname.setError("Please enter valid email address.");
        } else if (StringUtils.isTrimmedEmpty(mBinding.fRegisterEdtPassword.getText().toString())) {
            mBinding.inputLayoutPassword.setError("Please enter password");
        } else if (!StringUtils.isEquals(mBinding.fRegisterEdtPassword.getText().toString(), mBinding.fRegisterEdtRePassword.getText().toString())) {
            mBinding.inputLayoutRepassword.setError("Both password should be equal.");
        } else {
            loadRegUserResponse(mBinding.fRegisterEdtEmail.getText().toString(), mBinding.fRegisterEdtPassword.getText().toString());
        }
    }


    /**
     * Name : registerUser
     * <br> Purpose : This method will be called from the {@link RegisterFragment}
     * to register a new user on server.
     *
     * @param sEmail    : E-Mail ID entered by the user.
     * @param sPassword : Password entered by the user.
     */
    public LiveData<ApiResponse> loadRegUserResponse(final String sEmail, final String sPassword) {
        if (!NetworkUtils.isNetworkAvailable(mContext))
            mRegView.noInternetConnection(() -> loadRegUserResponse(sEmail, sPassword));
        else {
            mRegView.showLoader(mContext.getString(R.string.message_loader_registering));
            RegisterRequest mRegisterRequest = new RegisterRequest();
            mRegisterRequest.setEmail(sEmail);
            mRegisterRequest.setPassword(sPassword);
            mRegLiveData.addSource(
                    mHomeRepo.registerUserOnServer(mRegisterRequest), apiResponse -> mRegLiveData.setValue(apiResponse)
            );
        }
        return mRegLiveData;
    }


}

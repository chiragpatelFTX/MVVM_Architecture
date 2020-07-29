package com.ftx.mvvm_template.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.FragmentRegisterBinding;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.model.entities.response.RegisterResponse;
import com.ftx.mvvm_template.mvvm.viewModels.RegisterViewModel;
import com.ftx.mvvm_template.mvvm.views.RegisterView;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.views.activities.AppBaseActivity;


/**
 * Name : RegisterFragment
 * <br> Purpose :
 * This class is used for register fragment.
 * to show the register form to create a new member on our application.
 */
public class RegisterFragment extends BaseFragment2<FragmentRegisterBinding, RegisterViewModel> implements RegisterView {

    private RegisterViewModel mRegViewModel;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public RegisterViewModel getViewModel() {
        if (mRegViewModel == null)
            mRegViewModel = (RegisterViewModel) getViewModel(RegisterViewModel.class)
                    .inIt(mContext, this);
        return mRegViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getmViewDataBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRegViewModel.regLiveData().observe((LifecycleOwner) mContext, apiResponse -> {
            hideLoader();
            if (apiResponse != null) {
                if (apiResponse.getSingalData() instanceof APIError) {
//                      HandleError
                    apiError((APIError) apiResponse.getSingalData());
                } else {
                    RegisterResponse mRegisterResponse = (apiResponse.getSingalData() instanceof RegisterResponse) ?
                            (RegisterResponse) apiResponse.getSingalData() : null;
                    if (mRegisterResponse != null) {
                        onUserRegistered(mRegisterResponse);
                    } else {
                        apiError(new APIError(-1, mContext.getString(R.string.error_userRegistration)));
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * Name : RegisterFragment onUserRegistered
     * <br> Purpose :
     * This method will be executed by the register presenter.
     * when user registered successfully and navigates user to home page.
     *
     * @param mResponse :  Object of {@link RegisterResponse}
     */
    public void onUserRegistered(RegisterResponse mResponse) {
        AppLog.e(TAG, "RegisterResponse : " + mResponse.getToken());
        Log.d(TAG, "User registerd successfully");
        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                HomeFragment.class.getSimpleName(),
                HomeFragment.class,
                HomeFragment.class.getSimpleName(), null);

        ((AppBaseActivity) getActivity()).clearAllTopFragment();

        ((AppBaseActivity) getActivity()).setFragment(mChangeAssessment);
    }

    @Override
    public void onRegisterClicked() {
        mRegViewModel.validateForm(getmViewDataBinding());
    }
}

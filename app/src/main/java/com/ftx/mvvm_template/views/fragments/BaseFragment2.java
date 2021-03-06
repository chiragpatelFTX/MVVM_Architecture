package com.ftx.mvvm_template.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.ftx.mvvm_template.BR;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.mvvm.viewModels.BaseViewModel;
import com.ftx.mvvm_template.mvvm.views.BaseView;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.utils.CommonUtils;
import com.ftx.mvvm_template.utils.StringUtils;
import com.ftx.mvvm_template.views.listeners.NetworkRetryCallback;


/**
 * Name : BaseFragment
 * <br> Purpose :
 * This class is extended by all the fragments.
 * it contains the method which are implemented by the base view as well as
 * get current context etc...
 */
public abstract class BaseFragment2<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment implements BaseView {

    String TAG = "BaseFragment";
    private Dialog mProgressDialog;
    private T mViewDataBinding;

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract V getViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(BR.viewModel, getViewModel());
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public void onDestroyView() {
        AppLog.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        AppLog.d(TAG, "onDestroy");
        super.onDestroy();
    }


    /**
     * @param viewModelClass = Generic View Model class instance
     * @param <T>
     * @return Return appropriate view model class instance
     */
    public <T extends ViewModel> T getViewModel(Class<T> viewModelClass) {
        return ViewModelProviders.of(this).get(viewModelClass);
    }

    /**
     * Name : BaseFragment getCurrentContext
     * <br> Purpose :
     * This method is used to get current context of the application.
     *
     * @return : {@link Context} of current fragment/activity.
     */
    @Override
    public Context getCurrentContext() {
        return getActivity();
    }


    /**
     * Name : BaseFragment onStart
     */
    @Override
    public void onStart() {
        AppLog.e(TAG, "onStart");
        super.onStart();
    }


    /**
     * Name : BaseFragment onStop
     */
    @Override
    public void onStop() {
        AppLog.e(TAG, "onStop");
        super.onStop();
    }


    /**
     * Name : BaseFragment showLoader
     * <br> Purpose :This method is used to show loader when any API calls. or in any other place
     * in fragment.
     *
     * @param aMessage : message which we want to display in loaders.
     */
    @Override
    public void showLoader(String... aMessage) {
        mProgressDialog = CommonUtils.showProgressDialog(getCurrentContext(), aMessage);
        mProgressDialog.show();
    }


    /**
     * Name : BaseFragment hideLoader
     * <br> Purpose :This method will used by fragment to hide loaders.
     */
    @Override
    public void hideLoader() {
        CommonUtils.hideProgressDialog(mProgressDialog);
    }


    /**
     * Name : BaseFragment noInternetConnection
     * <br> Purpose :
     * This method will handle the no InternetConnection. before making
     * any Network API Call.
     *
     * @param aNetworkRetryCallback : callback to handle the retry mechanism. when internet comes back.
     */
    @Override
    public void noInternetConnection(NetworkRetryCallback aNetworkRetryCallback) {
        CommonUtils.showNetworkDialog(getCurrentContext(), aNetworkRetryCallback);
    }


    /**
     * Name : BaseFragment apiError
     * <br> Purpose :This method is used to show the error message
     *
     * @param aError Object of {@link APIError } which contains the error message and ErrorCode.
     */
    @Override
    public void apiError(APIError aError) {
        if (aError != null && !StringUtils.isTrimmedEmpty(aError.getStatusMessage()))
            CommonUtils.showToast(getCurrentContext(), aError.getStatusMessage());
    }

    @Override
    public void toast(String message) {
        CommonUtils.showToast(getCurrentContext(), message);
    }
}

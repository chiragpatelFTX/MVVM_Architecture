package com.ftx.mvvm_template.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.ftx.mvvm_template.framework.model.APIError;
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
public class BaseFragment extends Fragment implements BaseView {

    String TAG = "BaseFragment";
    private Dialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.e(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        AppLog.e(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        AppLog.e(TAG, "onDestroy");
        super.onDestroy();
    }

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
     * <br> Purpose : onStart of the fragment register the event bus.
     */
    @Override
    public void onStart() {
        AppLog.e(TAG, "onStart");
        super.onStart();
    }


    /**
     * Name : BaseFragment onStop
     * <br> Purpose : OnStop of the fragment unregister eventbus.
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
            Toast.makeText(getCurrentContext(), aError.getStatusMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void toast(String message) {
        Toast.makeText(getCurrentContext(), message, Toast.LENGTH_SHORT).show();
    }
}

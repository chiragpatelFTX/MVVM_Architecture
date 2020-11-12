package com.ftx.mvvm_template.views.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.ftx.mvvm_template.BR;
import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.ActivityBaseBinding;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.mvvm.viewModels.BaseViewModel;
import com.ftx.mvvm_template.mvvm.views.BaseView;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.utils.CommonUtils;
import com.ftx.mvvm_template.utils.StringUtils;
import com.ftx.mvvm_template.views.listeners.NetworkRetryCallback;


/**
 * Name : AppBaseActivity
 * <br> Purpose :
 * This class will contains the base activity which is
 * extended by all other activities.
 * <p>
 * It contains toolbar, methods for setting fragments, Back Handling mechanism Etc.
 */
public abstract class AppBaseActivity2<T extends ViewDataBinding, V extends BaseViewModel>
        extends BaseActivity implements BaseView {

    ActivityBaseBinding mBinding;
    private String TAG = AppBaseActivity2.class.getSimpleName();
    private String prevTag;
    private Dialog mProgressDialog;
    private T mViewDataBinding;
    private V mViewModel;

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract V getViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();

    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(BR.viewModel, mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
//        mViewDataBinding.executePendingBindings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public <T extends ViewModel> T getViewModel(Class<T> viewModelClass) {
        return ViewModelProviders.of(this).get(viewModelClass);
    }

    /**
     * Name : AppBaseActivity setFragment
     * <br> Purpose :
     * This method will be used to set the fragment to the container.
     * and also manage the backStackEntryCount of the fragment.
     *
     * @param sNavItemModel : Object of {@link NavItemModel} which contains the info related to fragments
     *                      which we want to set into container.
     */
    public void setFragment(NavItemModel sNavItemModel) {
        AppLog.d(TAG, "setSelectedNavItem():" + sNavItemModel.getFragment().getSimpleName());
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        loadFragment(sNavItemModel, mFragmentTransaction);
        mFragmentTransaction.commit();

        AppLog.i("Count", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        // SETUP THE BACK BUTTON.
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


    /**
     * Name : AppBaseActivity loadFragment
     * <br> Purpose :
     * This method will be executed by setFragment method. and this method will
     * first detach the existing fragment from container and then set new fragment to the container.
     * and also add fragment to backstack.
     *
     * @param aNavItemModel        : Object of {@link NavItemModel} which we want to set in container.
     * @param aFragmentTransaction : Object of {@link FragmentTransaction} to manage the transaction
     *                             between switching of fragments.
     */
    public void loadFragment(NavItemModel aNavItemModel, FragmentTransaction aFragmentTransaction) {
        Fragment fragment;

        if (prevTag != null && !prevTag.equals(aNavItemModel.getTag())) {
            fragment = getSupportFragmentManager().findFragmentByTag(prevTag);
            if (fragment != null) {
                aFragmentTransaction.detach(fragment);
            }
        }

        fragment = getSupportFragmentManager().findFragmentByTag(aNavItemModel.getTag());

        if (fragment == null) {
            fragment = Fragment.instantiate(this, aNavItemModel.getFragment().getName(), aNavItemModel.getBundle());
            aFragmentTransaction.replace(R.id.flContainer, fragment, aNavItemModel.getTag());
        } else if (fragment.isDetached()) {
            fragment = Fragment.instantiate(this, aNavItemModel.getFragment().getName(), aNavItemModel.getBundle());
            aFragmentTransaction.replace(R.id.flContainer, fragment, aNavItemModel.getTag());
        } else {
            fragment = Fragment.instantiate(this, aNavItemModel.getFragment().getName(), aNavItemModel.getBundle());
            aFragmentTransaction.replace(R.id.flContainer, fragment, aNavItemModel.getTag());
        }

        aFragmentTransaction.addToBackStack(null);
        prevTag = aNavItemModel.getTag();
        AppLog.e("DATA", "prevtag " + prevTag);


    }


    /**
     * Name : AppBaseActivity detachFragment
     * <br> Purpose :
     * This method is used to detach the specific fragment. from the backstack of the fragment
     * transaction.
     *
     * @param aNavItemModel        : Object of {@link NavItemModel} which we want to set in container.
     * @param aFragmentTransaction : Object of {@link FragmentTransaction} to manage the transaction
     *                             between switching of fragments.
     */
    private void detachFragment(NavItemModel aNavItemModel, FragmentTransaction aFragmentTransaction) {
        AppLog.d(TAG, "detachFragment() aNavItemModel:" + aNavItemModel.getTag());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(aNavItemModel.getTag());
        if (fragment != null && !fragment.isDetached()) {
            aFragmentTransaction.detach(fragment);
        }
    }

    /**
     * Name : AppBaseActivity hideSoftKeybord
     * <br> Purpose :This method is used to hide the soft keyboard forcefully.
     */
    public void hideSoftKeybord() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    /**
     * Name : AppBaseActivity clearAllTopFragment
     * <br> Purpose :This method is used to clear all the fragments of the backstack.
     */
    public void clearAllTopFragment() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Name : AppBaseActivity onBackPressed
     * <br> Purpose :This method will handle the back button pressed event.
     */
    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStackImmediate();
            }
        } else {
            super.onBackPressed();
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getCurrentContext() {
        return this;
    }

    @Override
    public void showLoader(String... aMessage) {
        mProgressDialog = CommonUtils.showProgressDialog(getCurrentContext(), aMessage);
        mProgressDialog.show();
    }

    @Override
    public void hideLoader() {
        CommonUtils.hideProgressDialog(mProgressDialog);
    }

    @Override
    public void noInternetConnection(NetworkRetryCallback aNetworkRetryCallback) {
        CommonUtils.showNetworkDialog(getCurrentContext(), aNetworkRetryCallback);
    }

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

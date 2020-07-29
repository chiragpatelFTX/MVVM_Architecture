package com.ftx.mvvm_template.mvvm.views;

import com.ftx.mvvm_template.databinding.ActivityDetailBinding;
import com.ftx.mvvm_template.views.fragments.LoginFragment;

/**
 * Name : LoginView
 * <br> Purpose :
 * This Interface will contains all the methods which
 * are implemented by the {@link LoginFragment}
 * on API Responses.
 */
public interface DetailView extends BaseView {

    void goBack();

    ActivityDetailBinding getBindingVariable();

}

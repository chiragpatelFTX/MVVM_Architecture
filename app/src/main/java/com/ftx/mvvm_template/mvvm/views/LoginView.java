package com.ftx.mvvm_template.mvvm.views;

import com.ftx.mvvm_template.model.entities.response.LoginResponse;
import com.ftx.mvvm_template.views.fragments.LoginFragment;


/**
 * Name : LoginView
 * <br> Purpose :
 * This Interface will contains all the methods which
 * are implemented by the {@link LoginFragment}
 * on API Responses.
 */
public interface LoginView extends BaseView {

    void onLoginSuccess(LoginResponse mResponse);
}

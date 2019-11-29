package com.sql.projecttemplate.mvvm.views;

import com.sql.projecttemplate.model.entities.response.LoginResponse;
import com.sql.projecttemplate.views.fragments.LoginFragment;


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

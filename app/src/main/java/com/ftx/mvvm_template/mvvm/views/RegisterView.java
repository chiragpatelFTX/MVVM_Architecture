package com.ftx.mvvm_template.mvvm.views;

import com.ftx.mvvm_template.model.entities.response.RegisterResponse;
import com.ftx.mvvm_template.views.fragments.RegisterFragment;

/**
 * Name : RegisterView
 * <br> Purpose :
 * This Interface will contains all the methods which
 * are implemented by the {@link RegisterFragment}
 * on API Responses.
 */
public interface RegisterView extends BaseView {

    void onRegisterClicked();
}

package com.sql.projecttemplate.mvvm.views;

import com.sql.projecttemplate.model.entities.response.RegisterResponse;
import com.sql.projecttemplate.views.fragments.RegisterFragment;

/**
 * Name : RegisterView
 * <br> Purpose :
 * This Interface will contains all the methods which
 * are implemented by the {@link RegisterFragment}
 * on API Responses.
 */
public interface RegisterView extends BaseView {

    void onUserRegistered(RegisterResponse mResponse);
}

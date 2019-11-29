package com.sql.projecttemplate.mvvm.views;

import android.content.Context;

import com.sql.projecttemplate.framework.model.APIError;
import com.sql.projecttemplate.views.listeners.NetworkRetryCallback;

/**
 * Base view which can be extend to all other views of MVP
 *
 * @author hrdudhat
 */
public interface MvvmView {

    /**
     * Override to get current context object.
     */
    Context getCurrentContext();
    /**
     * Override to show loader for long running task
     */
    void showLoader(String... aMessage);

    /**
     * Override to hide loader of long running task
     */
    void hideLoader();

    /**
     * override to show error message when internet connection is not available
     */
    void noInternetConnection(NetworkRetryCallback mRetryCallback);

    /**
     * To show api error messages. This will be called mostly from onApiError of Presenter class
     *
     * @param aError error message to show user
     */
    void apiError(APIError aError);
}

package com.ftx.mvvm_template.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.views.activities.AppBaseActivity;
import com.ftx.mvvm_template.views.fragments.HomeFragment;

import org.json.JSONException;

import java.util.Arrays;


/**
 *      Name :  FacebookSignInHelper
 *      <br/> Created by Arpan Mehta 20-08-2020
 *      <br> Purpose :  Helper class to Sign in with facebook.
 *      App.Build Gradle for Sign in from different platform.
 *      implementation com.facebook.android:facebook-login:[5,6)'
 */

public class FacebookSignInHelper {
    private static final String TAG = FacebookSignInHelper.class.getSimpleName();
    private static final String[] FBPermission = {"email", "public_profile"/*, "user_birthday"*/};
    private Context mContext;
    // FACEBOOK
    private CallbackManager mCallbackManager;
    private AccessToken mFacebookToken;

    public FacebookSignInHelper(Context mContext) {
        this.mContext = mContext;
        setCallBackManager(mCallbackManager);
    }

    /**
     * Name : FacebookSignInHelper registerFacebookCallback
     * <br> Purpose :This method will register the callback manager while we do login with facebook.
     * @param mCallbackManager : callback manager of facebook to get the login success or not from facebook
     *                         and receive the access token.
     *
     * @implNote
     */
    public void initFacebookSignIn(CallbackManager mCallbackManager) {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setAccessToken(mFacebookToken, loginResult);
            }

            @Override
            public void onCancel() {
                new APIError(0, "You have cancelled the login with facebook.");
            }

            @Override
            public void onError(FacebookException error) {
                new APIError(error.hashCode(), error.getMessage());
            }
        });
    }

    /**
     * Name : FacebookSignInHelper getFBUserDetails
     * <br> Purpose : This method will get the user detail of the logged in user.
     * @param mFacebookToken : Accesstoken of facebook which we received after successful login.
     */
    public void getFBUserDetails(AccessToken mFacebookToken) {
        boolean isLoggedIn = mFacebookToken != null && !mFacebookToken.isExpired();
        AppLog.e(TAG, "isLoggedIn : " + isLoggedIn);
        if (!isLoggedIn) return;
        AppLog.e(TAG, "Facebook Token : " + mFacebookToken.getToken());
        GraphRequest request = GraphRequest.newMeRequest(
                mFacebookToken, (object, response) -> {
                    Log.d(TAG, object.toString());
                    try {
                        Log.e("FIRST NAME", object.getString("first_name"));
                        Log.e("LAST NAME", object.getString("last_name"));
                        Log.e("EMAIL", object.getString("email"));
                        String id = object.getString("id");
                        Log.e("ID", id);
                        Log.e("IMAGE", "https://graph.facebook.com/" + id + "/picture?type=normal");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AppLog.e(TAG, object.toString());
                    onUserLoggedinSuccess();
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
//        bundle.putString("fields", "id,name,link,email,birthday,first_name,last_name,gender,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Name : FacebookSignInHelper onUserLoggedinSuccess
     * <br> Purpose : This method will execute from presenter when user successfully loggedin.
     */
    public void onUserLoggedinSuccess() {
        // Redirect User to Home Page.

        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                HomeFragment.class.getSimpleName(),
                HomeFragment.class,
                HomeFragment.class.getSimpleName(), null);

        ((AppBaseActivity) mContext).clearAllTopFragment();

        ((AppBaseActivity) mContext).setFragment(mChangeAssessment);
    }

    /**
     * Name : FacebookSignInHelper getCallbackManager
     * <br> Purpose : This method will return mCallbackManager.
     */
    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    /**
     * Name : FacebookSignInHelper setCallBackManager
     * <br> Purpose : This method will sets callback manager.
     */
    public CallbackManager setCallBackManager(CallbackManager callbackManager) {
        try {
            callbackManager = CallbackManager.Factory.create();
            initFacebookSignIn(callbackManager);
            mCallbackManager = callbackManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return callbackManager;
    }

    /**
     * Name : FacebookSignInHelper getFacebookToken
     * <br> Purpose : This method will return mFacebookToken.
     */
    public AccessToken getFacebookToken() {
        return mFacebookToken;
    }

    /**
     * Name : FacebookSignInHelper setAccessToken
     * <br> Purpose : This method will sets AccessToken of facebook.
     */
    public AccessToken setAccessToken(AccessToken accessToken, LoginResult loginResult) {
        try {
            accessToken = loginResult.getAccessToken();
            getFBUserDetails(accessToken);
            mFacebookToken = accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }
}

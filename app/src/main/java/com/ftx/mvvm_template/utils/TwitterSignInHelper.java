package com.ftx.mvvm_template.utils;

import android.content.Context;
import android.util.Log;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.views.activities.AppBaseActivity;
import com.ftx.mvvm_template.views.fragments.HomeFragment;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;

/**
 *      Name :  TwitterSignInHelper
 *      <br> Purpose :  Helper class to Sign in with twitter.
 *      App.Build Gradle for Sign in from different platform.
 *      implementation appDependencies.glide
 *      implementation 'com.twitter.sdk.android:twitter-core:3.3.0'
 *      implementation 'com.twitter.sdk.android:tweet-ui:3.3.0'
 */

public class TwitterSignInHelper {
    private static final String TAG = TwitterSignInHelper.class.getSimpleName();
    private Context mContext;
    private TwitterAuthClient mAuthClient;
    private TwitterLoginButton mTwitterLoginButton;
    public TwitterSignInHelper(Context mContext) {
        this.mContext = mContext;
        initTwitterSignIn();
    }

    /**
     * Name : TwitterSignInHelper initTwitterSignIn
     * <br/> Purpose : Initialize TwitterSignIn
     *
     * @implNote
     */
    private void initTwitterSignIn() {
        setAuthClient(mAuthClient);
        //To get the session
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        if (session != null) {
            TwitterAuthToken authToken = session.getAuthToken();
            String token = authToken.token;
            String secret = authToken.secret;
        }
        //To clear the session
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }
    /**
     * Name : TwitterSignInHelper registerTwitterCallback
     * <br/> Purpose : Register Twitter Call Back.
     * @param mTwitterLogin
     */
    public void registerTwitterCallback(TwitterLoginButton mTwitterLogin) {
        mTwitterLoginButton=mTwitterLogin;
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                getTwitterUserDetails(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    /**
     * Name : TwitterSignInHelper getTwitterUserDetails
     * <br/> Purpose : Get Twitter User Details.
     * @param session
     */
    private void getTwitterUserDetails(TwitterSession session) {
        mAuthClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                Log.e(TAG, "User Id : " + session.getUserId() + "\nScreen Name : " + session.getUserName() + "\nEmail Id : " + result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                // Do something with result, which provides a User
                User user = result.data;
                Log.e(TAG, "User Id : " + user.id + "\nUser Name : " + user.name + "\nEmail Id : " + user.email + "\nScreen Name : " + user.screenName);

                String imageProfileUrl = user.profileImageUrl;
                Log.e(TAG, "Data : " + imageProfileUrl);
                //NOTE : User profile provided by twitter is very small in size i.e 48*48
                //Link : https://developer.twitter.com/en/docs/accounts-and-users/user-profile-images-and-banners
                //so if you want to get bigger size image then do the following:
                imageProfileUrl = imageProfileUrl.replace("_normal", "");
                Log.e(TAG, "Data : " + imageProfileUrl);

                onUserLoggedinSuccess();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    /**
     * Name : LoginFragment onUserLoggedinSuccess
     * <br> Purpose : This method will execute from presenter when user successfully logged in.
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
     * Name : TwitterSignInHelper getAuthClient
     * <br> Purpose : This method will return mAuthClient.
     */
    public TwitterAuthClient getAuthClient() {
        return mAuthClient;
    }

    /**
     * Name : TwitterSignInHelper setAccessToken
     * <br> Purpose : This method will sets mAuthClient of Twitter.
     */
    private TwitterAuthClient setAuthClient(TwitterAuthClient authClient) {
        try {
            authClient = new TwitterAuthClient();
            mAuthClient = authClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authClient;
    }
}

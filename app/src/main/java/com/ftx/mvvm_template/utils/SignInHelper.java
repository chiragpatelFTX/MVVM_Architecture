package com.ftx.mvvm_template.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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

import org.json.JSONException;

import retrofit2.Call;

/**
 *       Name : SignInHelper
 *       <br> Purpose : Class use for login with(Google/Facebook/Twitter)
 *      App.Build Gradle for Sign in from different platform.
 *      Google:- implementation 'com.google.android.gms:play-services-auth:18.1.0'
 *      Facebook:-implementation 'com.facebook.android:facebook-login:[5,6)'
 *      Twitter:-implementation appDependencies.glide
 *               implementation 'com.twitter.sdk.android:twitter-core:3.3.0'
 *               implementation 'com.twitter.sdk.android:tweet-ui:3.3.0'
 */

public class SignInHelper {
    private Context mContext;
    private String mSocialAppName="";

    private static final String TAG = SignInHelper.class.getSimpleName();

    // FACEBOOK
    private static final String[] FBPermission = {"email", "public_profile"/*, "user_birthday"*/};
    public CallbackManager mCallbackManager;
    public AccessToken mFacebookToken;

    //Google
    public static final int RC_SIGN_IN = 101;
    private boolean isLoggingOut = false;
    public GoogleSignInClient mGoogleSignInClient;

    //Twitter
    public TwitterAuthClient authClient;
    private TwitterLoginButton mTwitterLoginButton;

    public SignInHelper(Context mContext, String mSocialAppName) {
        this.mContext = mContext;
        this.mSocialAppName = mSocialAppName;
        mCallbackManager = CallbackManager.Factory.create();
        checkSocialAppName(mSocialAppName);
    }

    /**
     * Name : checkSocialAppName
     * <br> Purpose :
     * This method will check social app name while login.
     * @param mSocialAppName :like Sign in with(Google,Facebook,Twitter)
     */
    private void checkSocialAppName(String mSocialAppName) {
        if(mSocialAppName.equalsIgnoreCase(mContext.getResources().getString(R.string.app_facebook))){
            initFacebookSignIn(mCallbackManager);
        }else if(mSocialAppName.equalsIgnoreCase(mContext.getResources().getString(R.string.app_google))){
            initGoogleSignIn();
        }else if(mSocialAppName.equalsIgnoreCase(mContext.getResources().getString(R.string.app_twitter))){
            initTwitterSignIn();
        }
    }

    /**
     * Facebook
     * Name : LoginFragment registerFacebookCallback
     * <br> Purpose :
     * This method will register the callback manager while we do login with facebook.
     *
     * @param mCallbackManager : callback manager of facebook to get the login success or not from facebook
     *                         and receive the access token.
     */
    private void initFacebookSignIn(CallbackManager mCallbackManager) {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFacebookToken = loginResult.getAccessToken();
                getFBUserDetails(mFacebookToken);
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
     * Name : SignInHelper getFBUserDetails
     * <br> Purpose :
     * This method will get the user detail of the logged in user.
     *
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
     * Google Signin
     */
    private void initGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
        //updateUI(account);
        if (account != null) {
            Log.e(TAG, account.getEmail());
            Log.e(TAG, account.getDisplayName());
        }
    }

    public Intent performSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        return signInIntent;
    }


    public void getGoogleAccountDetails(GoogleSignInResult result) {
        // Google Sign In was successful, authenticate with FireBase
        GoogleSignInAccount account = result.getSignInAccount();
        // You are now logged into Google
    }
    /**
     * Name : SignInHelper handleSignInResult
     * <br/> Created by Darshan Rathod 20-07-2020
     * <br/> Modified by Darshan Rathod 20-07-2020
     * <br/> Purpose : check google signin result and update UI
     *
     * @implNote
     */
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.e(TAG, account.getEmail());
                Log.e(TAG, account.getDisplayName());
                onUserLoggedinSuccess();
            }
            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    /**
     * Twitter
     */
    private void initTwitterSignIn() {
        authClient = new TwitterAuthClient();
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

    private void getTwitterUserDetails(TwitterSession session) {
        authClient.requestEmail(session, new Callback<String>() {
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
     * <br> Purpose : This method will execute from presenter when user successfully loggedin.
     */
    public void onUserLoggedinSuccess() {
        mSocialAppName="";
        // Redirect User to Home Page.
        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                HomeFragment.class.getSimpleName(),
                HomeFragment.class,
                HomeFragment.class.getSimpleName(), null);

        ((AppBaseActivity) mContext).clearAllTopFragment();

        ((AppBaseActivity) mContext).setFragment(mChangeAssessment);

    }
}

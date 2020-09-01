package com.ftx.mvvm_template.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.views.activities.AppBaseActivity;
import com.ftx.mvvm_template.views.fragments.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *      Name :  GoogleSignInHelper
 *      <br/> Created by Arpan Mehta 20-08-2020
 *      <br> Purpose :  Helper class to Sign in with Google.
 *      App.Build Gradle for Sign in from different platform.
 *      implementation com.google.android.gms:play-services-auth18.1.0'
 */

public class GoogleSignInHelper  {
    private static final String TAG = GoogleSignInHelper.class.getSimpleName();
    private Context mContext;
    public static final int RC_SIGN_IN = 101;
    private boolean isLoggingOut = false;
    private GoogleSignInClient mGoogleSignInClient;
    public GoogleSignInHelper(Context mContext) {
        this.mContext = mContext;
        initGoogleSignIn();
    }


    /**
     * Name : GoogleSignInHelper initGoogleSignIn
     * <br/> Purpose : Initialize GoogleSignIn
     *
     * @implNote
     */
    private void initGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        setsGoogleSignInClient(mGoogleSignInClient, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
        //updateUI(account);
        if (account != null) {
            Log.e(TAG, account.getEmail());
            Log.e(TAG, account.getDisplayName());
        }
    }
    /**
     * Name : GoogleSignInHelper performSignIn
     * <br/> Purpose : Initialize GoogleSignIn Intent
     *
     * @implNote
     */
    public Intent performSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        return signInIntent;
    }

    /**
     * Name : GoogleSignInHelper handleSignInResult
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
     * Name : GoogleSignInHelper onUserLoggedinSuccess
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
     * Name : GoogleSignInHelper getGoogleSignInClient
     * <br> Purpose : This method will return mGoogleSignInClient.
     */
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    /**
     * Name : GoogleSignInHelper setGoogleSignInClient
     * <br> Purpose : This method will sets GoogleSignInClient.
     */
    private GoogleSignInClient setsGoogleSignInClient(GoogleSignInClient googleSignInClient, GoogleSignInOptions gso) {
        try {
            googleSignInClient = GoogleSignIn.getClient(mContext, gso);
            mGoogleSignInClient = googleSignInClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return googleSignInClient;
    }
}

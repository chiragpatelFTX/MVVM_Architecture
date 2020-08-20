package com.ftx.mvvm_template.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.FragmentLoginBinding;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.model.entities.response.LoginResponse;
import com.ftx.mvvm_template.mvvm.viewModels.LoginViewModel;
import com.ftx.mvvm_template.mvvm.views.LoginView;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.utils.FacebookSignInHelper;
import com.ftx.mvvm_template.utils.GoogleSignInHelper;
import com.ftx.mvvm_template.utils.TwitterSignInHelper;
import com.ftx.mvvm_template.views.activities.AppBaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;

import java.util.Arrays;

import retrofit2.Call;


/**
 * Name : LoginFragment
 * <br> Purpose :
 * This fragment is used for Login mechanism.
 * which override all the methods of the login view. and also navigates
 * per the login response and if there are any errors then we will display that error.
 */
public class LoginFragment extends BaseFragment2<FragmentLoginBinding, LoginViewModel> implements
        LoginView {

    private static final String[] FBPermission = {"email", "public_profile"/*, "user_birthday"*/};

    private static final int RC_SIGN_IN = 101;
    LoginViewModel mLoginViewModel;
    // FACEBOOK
    private CallbackManager mCallbackManager;
    private AccessToken mFacebookToken;
    private Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    private TwitterAuthClient authClient;

    //Facebook Sign-In Helper Class
    private FacebookSignInHelper facebookSignInHelper;
    //Google Sign-In Class
    private GoogleSignInHelper googleSignInHelper;
    //Twitter Sign-In Helper Class
    private TwitterSignInHelper twitterSignInHelper;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        if (mLoginViewModel == null)
            mLoginViewModel = (LoginViewModel) getViewModel(LoginViewModel.class).inIt(mContext, this);
        return mLoginViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
        // For Google Signin
        googleSignInHelper=new GoogleSignInHelper(mContext);
        // For Facebook
        facebookSignInHelper=new FacebookSignInHelper(mContext);
//        mCallbackManager = CallbackManager.Factory.create();
        // For Twitter Sign in
        twitterSignInHelper=new TwitterSignInHelper(mContext);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel.getApiResponse().observe(this, apiResponse -> {
            hideLoader();
            if (apiResponse == null) return;
            if (apiResponse.getSingalData() instanceof APIError) {
//                      HandleError
                apiError((APIError) apiResponse.getSingalData());
            } else {
                LoginResponse mLoginResponse = apiResponse.getSingalData() instanceof LoginResponse ? (LoginResponse) apiResponse.getSingalData() : new LoginResponse();
                if (mLoginResponse != null && !TextUtils.isEmpty(mLoginResponse.getToken())) {
                    Log.d(TAG, mLoginResponse.getToken());
                    onLoginSuccess();
                }
            }

        });
        twitterSignInHelper.registerTwitterCallback(getmViewDataBinding().btnTwitter);
    }

    /**
     * Name : LoginFragment onLoginSuccess
     * <br> Purpose :
     * This method will be executed by the presenter when we get the login response.
     * If login is success then we will navigate to home fragment.
     */
    public void onLoginSuccess() {

        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                HomeFragment.class.getSimpleName(),
                HomeFragment.class,
                HomeFragment.class.getSimpleName(), null);

        ((AppBaseActivity) getActivity()).clearAllTopFragment();
        ((AppBaseActivity) getActivity()).setFragment(mChangeAssessment);
    }

    @Override
    public void onLoginClicked() {
        mLoginViewModel.validateForm(getmViewDataBinding());
    }

    /**
     * Name : LoginFragment onDestroyView
     * <br> Purpose : unbind butterknife and stop the google api client on destroy.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        googleSignInHelper.mGoogleSignInClient.signOut();
    }

    /**
     * Name : onClickFacebookLogin
     * <br> Purpose : To get permission if not has and than this method will be used for get FacebookUser detail.
     */
    @Override
    public void onClickFacebookLogin() {
    /*    if (mFacebookToken == null)
            LoginManager.getInstance().logInWithReadPermissions
                    (LoginFragment.this, Arrays.asList(FBPermission));
        else
            getFBUserDetails(mFacebookToken);*/
        if (facebookSignInHelper.mFacebookToken == null)
            LoginManager.getInstance().logInWithReadPermissions
                    (LoginFragment.this, Arrays.asList(FBPermission));
        else
            facebookSignInHelper.getFBUserDetails(facebookSignInHelper.mFacebookToken);
    }

    /**
     * Name : onClickGmailLogin
     * <br> Purpose : This method will initiate google authentication and will return result in onActivityResult method.
     */
    @Override
    public void onClickGmailLogin() {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = googleSignInHelper.performSignIn();
        startActivityForResult(signInIntent, googleSignInHelper.RC_SIGN_IN);
    }

    /**
     * Name : onClickRegister
     * <br> Purpose : To redirect user to Register Screen
     */
    @Override
    public void onClickRegister() {
        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                RegisterFragment.class.getSimpleName(),
                RegisterFragment.class,
                RegisterFragment.class.getSimpleName(), null);
        ((AppBaseActivity) getActivity()).setFragment(mChangeAssessment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookSignInHelper.mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == googleSignInHelper.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleSignInHelper.handleSignInResult(task);
            return;
        }
        // Pass the activity result to the twitterAuthClient.
        if (twitterSignInHelper.authClient != null)
            twitterSignInHelper.authClient.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        getmViewDataBinding().btnTwitter.onActivityResult(requestCode, resultCode, data);
    }
}
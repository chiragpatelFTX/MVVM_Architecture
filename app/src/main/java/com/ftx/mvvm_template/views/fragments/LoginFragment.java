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
public class LoginFragment extends BaseFragment2<FragmentLoginBinding, LoginViewModel> implements LoginView {

    private static final String[] FBPermission = {"email", "public_profile"/*, "user_birthday"*/};

    private static final int RC_SIGN_IN = 101;
    LoginViewModel mLoginViewModel;
    // FACEBOOK
    private CallbackManager mCallbackManager;
    private AccessToken mFacebookToken;
    private Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    private TwitterAuthClient authClient;

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
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getCurrentContext(), gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getCurrentContext());
        //updateUI(account);
        if (account != null) {
            Log.e(TAG, account.getEmail());
            Log.e(TAG, account.getDisplayName());
        }
        // For Facebook
        mCallbackManager = CallbackManager.Factory.create();

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

        registerFacebookCallback(mCallbackManager);
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
                if (!TextUtils.isEmpty(mLoginResponse.getToken())) {
                    Log.d(TAG, mLoginResponse.getToken());
                    onLoginSuccess();
                }
            }

        });
        registerTwitterCallback();
    }

    private void registerTwitterCallback() {
        getViewDataBinding().btnTwitter.setCallback(new Callback<TwitterSession>() {
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
     * Name : LoginFragment registerFacebookCallback
     * <br> Purpose :
     * This method will register the callback manager while we do login with facebook.
     *
     * @param mCallbackManager : callback manager of facebook to get the login success or not from facebook
     *                         and receive the access token.
     */
    private void registerFacebookCallback(CallbackManager mCallbackManager) {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFacebookToken = loginResult.getAccessToken();
                getFBUserDetails(mFacebookToken);
            }

            @Override
            public void onCancel() {
                apiError(new APIError(0, "You have cancelled the login with facebook."));
            }

            @Override
            public void onError(FacebookException error) {
                apiError(new APIError(error.hashCode(), error.getMessage()));
            }
        });
    }


    /**
     * Name : LoginFragment getFBUserDetails
     * <br> Purpose :
     * This method will get the user detail of the logged in user.
     *
     * @param mFacebookToken : Accesstoken of facebook which we received after successful login.
     */
    private void getFBUserDetails(AccessToken mFacebookToken) {
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
        mLoginViewModel.validateForm(getViewDataBinding());
    }

    /**
     * Name : LoginFragment onDestroyView
     * <br> Purpose : unbind butterknife and stop the google api client on destroy.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGoogleSignInClient.signOut();
    }

    /**
     * Name : onClickFacebookLogin
     * <br> Purpose : To get permission if not has and than this method will be used for get FacebookUser detail.
     */
    @Override
    public void onClickFacebookLogin() {
        if (mFacebookToken == null)
            LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, Arrays.asList(FBPermission));
        else
            getFBUserDetails(mFacebookToken);
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
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            return;
        }
        // Pass the activity result to the twitterAuthClient.
        if (authClient != null)
            authClient.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result to the login button.
        getViewDataBinding().btnTwitter.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Name : LoginFragment handleSignInResult
     * <br/> Created by Darshan Rathod 20-07-2020
     * <br/> Modified by Darshan Rathod 20-07-2020
     * <br/> Purpose : check google signin result and update UI
     *
     * @implNote
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
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
     * Name : LoginFragment onUserLoggedinSuccess
     * <br> Purpose : This method will execute from presenter when user successfully loggedin.
     */
    public void onUserLoggedinSuccess() {
        // Redirect User to Home Page.

        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                HomeFragment.class.getSimpleName(),
                HomeFragment.class,
                HomeFragment.class.getSimpleName(), null);

        ((AppBaseActivity) getActivity()).clearAllTopFragment();

        ((AppBaseActivity) getActivity()).setFragment(mChangeAssessment);

    }

}

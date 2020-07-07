package com.ftx.mvvm_template.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;


/**
 * Name : LoginFragment
 * <br> Purpose :
 * This fragment is used for Login mechanism.
 * which override all the methods of the login view. and also navigates
 * per the login response and if there are any errors then we will display that error.
 */
public class LoginFragment extends BaseFragment2<FragmentLoginBinding, LoginViewModel> implements LoginView {

    private static final String[] FBPermission = {"email", "public_profile", "user_birthday"};

    private static final int RC_SIGN_IN = 101;
    LoginViewModel mLoginViewModel;
    // FACEBOOK
    private CallbackManager mCallbackManager;
    private AccessToken mFacebookToken;
    // FOR GOOGLE
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // For Facebook
        mCallbackManager = CallbackManager.Factory.create();
        registerFacebookCallback(mCallbackManager);

        // For Google.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage((FragmentActivity) mContext, connectionResult -> apiError(new APIError(connectionResult.getErrorCode(), connectionResult.getErrorMessage())))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return getmViewDataBinding().getRoot();
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
        AppLog.e(TAG, "Facebook Token : " + mFacebookToken.getToken());
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,link,email,birthday,first_name,last_name,gender,picture.type(large)");
        GraphRequest mRequest = new GraphRequest(mFacebookToken, "/me", bundle, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject mFBUserObject = response.getJSONObject();
                AppLog.e(TAG, mFBUserObject.toString());
                onUserLoggedinSuccess();
            }
        });

        mRequest.executeAsync();
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
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
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
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            onGoogleSignInResult(result);
            return;
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Name : LoginFragment onGoogleSignInResult
     * <br> Purpose :
     * This method will handle the result from google signin.
     *
     * @param result : object of {@link GoogleSignInResult} which we received after login with google.
     */
    private void onGoogleSignInResult(GoogleSignInResult result) {
        AppLog.d(TAG, "handleSignInResult:" + result.isSuccess() + result.getStatus().getStatusCode());
        if (result.isSuccess()) {
            AppLog.e(TAG, result.getSignInAccount().getDisplayName());
            onUserLoggedinSuccess();
        } else {
            apiError(new APIError(0, "We\'ve cancelled the google sign in."));
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

package com.ftx.mvvm_template.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.model.entities.NavItemModel;
import com.ftx.mvvm_template.views.fragments.LoginFragment;


/**
 * Name : HomeActivity
 * <br> Purpose : Home activity which contains all the fragments of applications.
 */
public class HomeActivity extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         *  When we launch app first time and display home Activity then we need to
         *  set first fragment.
         */
        NavItemModel mChangeAssessment = new NavItemModel(
                R.mipmap.ic_launcher,
                LoginFragment.class.getSimpleName(),
                LoginFragment.class,
                LoginFragment.class.getSimpleName(), null);

        setFragment(mChangeAssessment);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

package com.sql.projecttemplate.views.activities;

import android.os.Bundle;

import com.sql.projecttemplate.R;
import com.sql.projecttemplate.model.entities.NavItemModel;
import com.sql.projecttemplate.views.fragments.LoginFragment;


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
}

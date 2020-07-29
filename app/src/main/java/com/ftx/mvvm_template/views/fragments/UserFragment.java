package com.ftx.mvvm_template.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.FragmentUsersBinding;
import com.ftx.mvvm_template.mvvm.viewModels.HomeViewModel;
import com.ftx.mvvm_template.mvvm.views.HomeView;
import com.ftx.mvvm_template.views.adapters.UserAdapter;

/**
 * Name : UserFragment
 * <br> Purpose : Display UserModel list and user can navigate to detail screen on item click
 */

public class UserFragment extends BaseFragment2<FragmentUsersBinding, HomeViewModel> implements HomeView {

    private HomeViewModel mHomeViewModel;
    private Context mContext;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    public HomeViewModel getViewModel() {
        if (mHomeViewModel == null)
            mHomeViewModel = (HomeViewModel) getViewModel(HomeViewModel.class).inIt(mContext, this);
        return mHomeViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getmViewDataBinding().setAdapter(new UserAdapter(mContext));
        return getmViewDataBinding().getRoot();
    }
}

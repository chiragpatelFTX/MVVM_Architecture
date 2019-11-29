package com.sql.projecttemplate.views.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sql.projecttemplate.R;
import com.sql.projecttemplate.databinding.FragmentUsersBinding;
import com.sql.projecttemplate.mvvm.viewModels.HomeViewModel;
import com.sql.projecttemplate.mvvm.views.HomeView;
import com.sql.projecttemplate.views.adapters.UserAdapter;

/**
 * Name : UserFragment
 * <br> Purpose : Display UserModel list and user can navigate to detail screen on item click
 */

public class UserFragment extends BaseFragment implements HomeView {

    private View mRootView;
    private FragmentUsersBinding mBinding;
    private HomeViewModel mHomeViewModel;
    private Context mContext;
    private UserAdapter mUserAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false);
        mRootView = mBinding.getRoot();
        mBinding.setEvent(this);

        mUserAdapter = new UserAdapter(mContext);
        mBinding.rclUserData.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rclUserData.setAdapter(mUserAdapter);

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get ViewModel class instance
        mHomeViewModel = (HomeViewModel) getViewModel(HomeViewModel.class).inIt(mContext, this);

        //Call Method from viewModel class for getUser list from server
//        mHomeViewModel.loadUserResponse();

        //Observe LiveData response and get updated data and also update UI .
        mHomeViewModel.getPagedUserList().observe(this, userList -> {
            if (mUserAdapter != null) {
                mUserAdapter.setList(userList);
            }
        });
    }
}

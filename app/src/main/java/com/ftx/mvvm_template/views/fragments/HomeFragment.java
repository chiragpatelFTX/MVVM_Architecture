package com.ftx.mvvm_template.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.FragmentHomeBinding;
import com.ftx.mvvm_template.mvvm.viewModels.HomeViewModel;
import com.ftx.mvvm_template.mvvm.views.HomeView;
import com.ftx.mvvm_template.views.adapters.PagerAdapter;


/**
 * Name : HomeFragment
 * <br> Purpose : This fragment is used to display the home page of the application after login.
 * also contains the method to get album list.
 */
public class HomeFragment extends BaseFragment2<FragmentHomeBinding, HomeViewModel> implements HomeView {

    private HomeViewModel mHomeViewModel;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        if (mHomeViewModel == null)
            mHomeViewModel = (HomeViewModel) getViewModel(HomeViewModel.class).inIt(mContext, this);
        return mHomeViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setupViewPager();
        return getmViewDataBinding().getRoot();
    }

    /**
     * Name : setupViewPager
     * <br> Purpose : To set fragments in viewPager Adapter
     */
    private void setupViewPager() {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AlbumFragment(), "Albums");
        adapter.addFragment(new UserFragment(), "Users");
        getmViewDataBinding().viewPager.setAdapter(adapter);
        getmViewDataBinding().resultTabs.setupWithViewPager(getmViewDataBinding().viewPager);
    }
}

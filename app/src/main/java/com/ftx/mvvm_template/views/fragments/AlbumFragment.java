package com.ftx.mvvm_template.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.FragmentAlbumsBinding;
import com.ftx.mvvm_template.mvvm.viewModels.HomeViewModel;
import com.ftx.mvvm_template.mvvm.views.HomeView;
import com.ftx.mvvm_template.views.adapters.AlbumAdapter;

/**
 * Name : AlbumFragment
 * <br> Purpose : Display AlbumModel list and user can navigate to detail screen on item click
 */

public class AlbumFragment extends BaseFragment2<FragmentAlbumsBinding, HomeViewModel> implements HomeView {

    private HomeViewModel mHomeViewModel;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_albums;
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
        getmViewDataBinding().setAdapter(new AlbumAdapter(mContext));
        return getmViewDataBinding().getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get ViewModel class instance

        //Call Method from viewModel class for getAlbum list from server
        mHomeViewModel.loadAlbumResponse();
    }
}

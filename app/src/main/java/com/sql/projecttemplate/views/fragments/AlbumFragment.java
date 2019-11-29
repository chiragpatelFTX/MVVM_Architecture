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
import com.sql.projecttemplate.databinding.FragmentAlbumsBinding;
import com.sql.projecttemplate.mvvm.viewModels.HomeViewModel;
import com.sql.projecttemplate.mvvm.views.HomeView;
import com.sql.projecttemplate.views.adapters.AlbumAdapter;

/**
 * Name : AlbumFragment
 * <br> Purpose : Display AlbumModel list and user can navigate to detail screen on item click
 */

public class AlbumFragment extends BaseFragment implements HomeView {


    private View mRootView;
    private FragmentAlbumsBinding mBinding;
    private HomeViewModel mHomeViewModel;
    private Context mContext;
    private AlbumAdapter mAlbumAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_albums, container, false);
        mRootView = mBinding.getRoot();
        mBinding.setEvent(this);

        mAlbumAdapter = new AlbumAdapter(mContext);
        mBinding.rclAlbumData.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rclAlbumData.setAdapter(mAlbumAdapter);

        return mRootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get ViewModel class instance
        mHomeViewModel = (HomeViewModel) getViewModel(HomeViewModel.class).inIt(mContext, this);

        //Call Method from viewModel class for getAlbum list from server
        mHomeViewModel.loadAlbumResponse();

        //Observe LiveData response and get updated data and also update UI .
        mHomeViewModel.getPagedAlbumList().observe(this, albumList -> {
            if (mAlbumAdapter != null) {
                mAlbumAdapter.setList(albumList);
            }
        });
    }
}

package com.ftx.mvvm_template.views.activities;

import android.os.Bundle;
import android.text.InputType;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.ActivityDetailBinding;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.mvvm.viewModels.DetailViewModel;
import com.ftx.mvvm_template.mvvm.views.DetailView;
import com.ftx.mvvm_template.utils.Constants;


/**
 * Name : DetailActivity
 * <br> Purpose : Use to update and delete User/Album detail .
 */

public class DetailActivity extends AppBaseActivity2<ActivityDetailBinding, DetailViewModel> implements DetailView {

    private Bundle mBundle;
    private int iAlbumId, iUserId;
    private ActivityDetailBinding mBinding;
    private DetailViewModel mDetailViewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public DetailViewModel getViewModel() {
        if (mDetailViewModel == null)
            mDetailViewModel = (DetailViewModel) getViewModel(DetailViewModel.class)
                    .inIt(this, this);
        return mDetailViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewDataBinding();
        setSupportActionBar(mBinding.toolbar);
        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            if (mBundle.containsKey(Constants.IntentKey.KEY_ALBUMID)) {
                iAlbumId = mBundle.getInt(Constants.IntentKey.KEY_ALBUMID);
                mBinding.setAlbumModel(new AlbumModel());
                if (iAlbumId != 0) {
                    // To Initialize xml data
                    mBinding.edtUserId.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mDetailViewModel.getAlbumModelFromDb(iAlbumId);
                }
            } else if (mBundle.containsKey(Constants.IntentKey.KEY_USERID)) {
                iUserId = mBundle.getInt(Constants.IntentKey.KEY_USERID);
                mBinding.setUserModel(new UserModel());
                if (iUserId != 0) {
                    // To Initialize xml data
                    mBinding.edtUserId.setInputType(InputType.TYPE_CLASS_TEXT);
                    mDetailViewModel.getUserModelFromDb(iUserId);
                }
            }
        }
    }

    @Override
    public ActivityDetailBinding getBindingVariable() {
        return mBinding;
    }

    @Override
    public void goBack() {
        finish();
    }
}

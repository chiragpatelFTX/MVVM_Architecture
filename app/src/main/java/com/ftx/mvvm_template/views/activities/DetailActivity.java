package com.ftx.mvvm_template.views.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Toast;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.ActivityDetailBinding;
import com.ftx.mvvm_template.framework.model.APIError;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.mvvm.viewModels.DetailViewModel;
import com.ftx.mvvm_template.mvvm.views.DetailView;
import com.ftx.mvvm_template.utils.Constants;
import com.ftx.mvvm_template.views.listeners.NetworkRetryCallback;


/**
 * Name : DetailActivity
 * <br> Purpose : Use to update and delete User/Album detail .
 */

public class DetailActivity extends AppBaseActivity implements DetailView {

    private Bundle mBundle;
    private int iAlbumId, iUserId;
    private ActivityDetailBinding mBinding;
    private Context mContext;
    private DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //Bind resource file.
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setSupportActionBar(mBinding.toolbar);

        //Set reference of this activity for access method of this class from xml for perform onClick event.
        mBinding.setEvent(this);

        mDetailViewModel = (DetailViewModel) getViewModel(DetailViewModel.class)
                .inIt(mContext, this);

        mBundle = getIntent().getExtras();

        //Get database instance from Application class
//        mDatabase =  TemplateApplication.getAppInstance().getDbInstance(mContext);

        if (mBundle != null) {
            if (mBundle.containsKey(Constants.IntentKey.KEY_ALBUMID)) {
                iAlbumId = mBundle.getInt(Constants.IntentKey.KEY_ALBUMID);
                mBinding.setAlbumModel(new AlbumModel());
                if (iAlbumId != 0) {
                    // To Initialize xml data
                    mBinding.edtUserId.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mDetailViewModel.getAlbumModelFromDb(iAlbumId, mBinding);
                }
            } else if (mBundle.containsKey(Constants.IntentKey.KEY_USERID)) {
                iUserId = mBundle.getInt(Constants.IntentKey.KEY_USERID);
                mBinding.setUserModel(new UserModel());
                if (iUserId != 0) {
                    // To Initialize xml data
                    mBinding.edtUserId.setInputType(InputType.TYPE_CLASS_TEXT);
                    mDetailViewModel.getUserModelFromDb(iUserId, mBinding);
                }
            }
        }

    }


    /**
     * Name : updateAlbumModel
     * <br> Purpose : To update AlbumModel Data
     */
    public void updateAlbumModel() {
        //For access resource use mBinding "object.resourseName"
        String sTitle = mBinding.edtTitle.getText().toString().trim();
        String sUserId = mBinding.edtUserId.getText().toString().trim();

        if (!TextUtils.isEmpty(sTitle)) {
            if (!TextUtils.isEmpty(sUserId)) {
                mDetailViewModel.updateAlbumData(sTitle, sUserId);
            } else {
                Toast.makeText(this, R.string.msg_enterTitle, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.msg_enterUserId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Name : deleteAlbumModel
     * <br> Purpose : To delete AlbumModel Data
     */
    public void deleteAlbumModel() {
        //To delete AlbumData
        mDetailViewModel.deleteAlbumData();
    }


    /**
     * Name : updateUserModel
     * <br> Purpose : To update UserModel Data
     */
    public void updateUserModel() {
        //For access resource use mBinding "object.resourseName"
        String sFirstName = mBinding.edtUserId.getText().toString().trim();    //FirstName
        String sLastName = mBinding.edtTitle.getText().toString().trim();      //LastName

        if (!TextUtils.isEmpty(sFirstName)) {
            if (!TextUtils.isEmpty(sLastName)) {
                //Update single row UserModel
                mDetailViewModel.updateUserData(sFirstName, sLastName);
            } else {
                Toast.makeText(this, R.string.msg_enterLastName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.msg_enterFirstName, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Name : deleteUserModel
     * <br> Purpose : To delete UserModel Data
     */
    public void deleteUserModel() {
        //To delete UserData
        mDetailViewModel.deleteUserModel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onAlbumUpdate() {
        finish();
    }

    @Override
    public void onAlbumDelete() {
        finish();
    }

    @Override
    public void onUserUpdate() {
        finish();
    }

    @Override
    public void onUserDelete() {
        finish();
    }

    @Override
    public Context getCurrentContext() {
        return mContext;
    }

    @Override
    public void showLoader(String... aMessage) {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void noInternetConnection(NetworkRetryCallback mRetryCallback) {

    }

    @Override
    public void apiError(APIError aError) {

    }
}

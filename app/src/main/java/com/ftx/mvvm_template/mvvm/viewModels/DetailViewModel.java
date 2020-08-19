package com.ftx.mvvm_template.mvvm.viewModels;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.databinding.ActivityDetailBinding;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.mvvm.views.DetailView;
import com.ftx.mvvm_template.mvvm.views.MvvmView;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.utils.Constants;
import com.ftx.mvvm_template.utils.DatabaseAsync;

/**
 * Name : LoginViewModel
 * <br> Purpose :  The LoginViewModel extends ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
 * The ViewModel class allows data to survive configuration changes such as screen rotations.
 */

public class DetailViewModel extends BaseViewModel {

    public static final String TAG = BaseViewModel.class.getName();

    public Context mContext;
    AlbumModel mAlbumModel;
    UserModel mUserModel;
    private MyDatabase mDatabase;
    private DetailView mDetailView;
    public MutableLiveData<String> toastMessage;

    // No argument constructor
    public DetailViewModel() {
    }

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        this.mContext = mContext;
        this.mDetailView = (DetailView) mvpView;
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();
        toastMessage = new MutableLiveData<>();
        return super.inIt(mContext, mvpView);
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    /**
     * Name : updateAlbumModel
     * <br> Purpose : To update AlbumModel Data
     */
    public void updateAlbumModel() {
        //For access resource use mBinding "object.resourseName"
        ActivityDetailBinding mBinding = mDetailView.getBindingVariable();
        String sTitle = mBinding.edtTitle.getText().toString().trim();
        String sUserId = mBinding.edtUserId.getText().toString().trim();
        if (TextUtils.isEmpty(sTitle)) {
            toastMessage.setValue(mContext.getString(R.string.msg_enterTitle));
            return;
        } else if (TextUtils.isEmpty(sUserId)) {
            toastMessage.setValue(mContext.getString(R.string.msg_enterUserId));
            return;
        }
        updateAlbumData(sTitle, sUserId);
    }

    /**
     * Name : deleteAlbumModel
     * <br> Purpose : To delete AlbumModel Data
     */
    public void deleteAlbumModel() {
        //To delete AlbumData
        deleteAlbumData();
    }


    /**
     * Name : updateUserModel
     * <br> Purpose : To update UserModel Data
     */
    public void updateUserModel() {
        //For access resource use mBinding "object.resourseName"
        ActivityDetailBinding mBinding = mDetailView.getBindingVariable();
        String sFirstName = mBinding.edtUserId.getText().toString().trim();    //FirstName
        String sLastName = mBinding.edtTitle.getText().toString().trim();      //LastName

        if (TextUtils.isEmpty(sFirstName)) {
            toastMessage.setValue(mContext.getString(R.string.msg_enterFirstName));
            return;
        } else if (TextUtils.isEmpty(sLastName)) {
            toastMessage.setValue(mContext.getString(R.string.msg_enterLastName));
            return;
        }
        updateUserData(sFirstName, sLastName);
    }


    /**
     * Name : deleteUserModel
     * <br> Purpose : To delete UserModel Data
     */
    public void deleteUserModel() {
        //To delete UserData
        deleteUserModelData();
    }


    public void getAlbumModelFromDb(int iAlbumId) {

        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                iAlbumId,
                Constants.Database.GET_ALBUM,
                result -> {
                    mAlbumModel = (AlbumModel) result;
                    mDetailView.getBindingVariable().setAlbumModel(mAlbumModel);
                }
        ).execute();

    }

    public void getUserModelFromDb(int iUserId) {

        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                iUserId,
                Constants.Database.GET_USER,
                result -> {
                    mUserModel = (UserModel) result;
                    mDetailView.getBindingVariable().setUserModel(mUserModel);
                }
        ).execute();
    }

    /**
     * Name : updateAlbumModel
     * <br> Purpose : To update AlbumModel Data
     */
    public void updateAlbumData(String sTitle, String sUserId) {

        if (mAlbumModel != null) {
            //For access resource use mBinding "object.resourseName"
            mAlbumModel.setTitle(sTitle);
            mAlbumModel.setUserId(Integer.parseInt(sUserId));
            AppLog.e(TAG, "UpdateData:>>>>>>>"+sTitle+" "+sUserId);
            new DatabaseAsync<AlbumModel>(mContext,
                    mDatabase,
                    mAlbumModel,
                    Constants.Database.UPDATE_ALBUM,
                    result -> mDetailView.goBack()
            ).execute();
        }
    }


    /**
     * Name : deleteAlbumModel
     * <br> Purpose : To delete AlbumModel Data
     */

    private void deleteAlbumData() {
        //To delete AlbumData
        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                mAlbumModel,
                Constants.Database.DELETE_ALBUM,
                result -> mDetailView.goBack()
        ).execute();
    }


    /**
     * Name : updateUserModel
     * <br> Purpose : To update UserModel Data
     */
    public void updateUserData(String sFirstName, String sLastName) {
        //For access resource use mBinding "object.resourseName"

        if (mUserModel != null) {
            mUserModel.setFirstName(sFirstName);
            mUserModel.setLastName(sLastName);

            new DatabaseAsync<AlbumModel>(mContext,
                    mDatabase,
                    mUserModel,
                    Constants.Database.UPDATE_USER,
                    result -> mDetailView.goBack()
            ).execute();
        }
    }

    /**
     * Name : deleteUserModel
     * <br> Purpose : To delete UserModel Data
     */
    public void deleteUserModelData() {
        //To delete UserData
        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                mUserModel,
                Constants.Database.DELETE_USER,
                result -> mDetailView.goBack()
        ).execute();
    }
}
package com.sql.projecttemplate.mvvm.viewModels;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.sql.projecttemplate.TemplateApplication;
import com.sql.projecttemplate.databinding.ActivityDetailBinding;
import com.sql.projecttemplate.model.db.MyDatabase;
import com.sql.projecttemplate.model.db.models.AlbumModel;
import com.sql.projecttemplate.model.db.models.UserModel;
import com.sql.projecttemplate.mvvm.views.DetailView;
import com.sql.projecttemplate.mvvm.views.MvvmView;
import com.sql.projecttemplate.utils.Constants;
import com.sql.projecttemplate.utils.DatabaseAsync;

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

    // No argument constructor
    public DetailViewModel() {
    }

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        this.mContext = mContext;
        this.mDetailView = (DetailView) mvpView;
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();
        return super.inIt(mContext, mvpView);
    }


    public void getAlbumModelFromDb(int iAlbumId, ActivityDetailBinding mBinding) {

        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                iAlbumId,
                Constants.Database.GET_ALBUM,
                result -> {
                    mAlbumModel = (AlbumModel) result;
                    mBinding.setAlbumModel(mAlbumModel);
                }
        ).execute();

    }

    public void getUserModelFromDb(int iUserId, ActivityDetailBinding mBinding) {

        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                iUserId,
                Constants.Database.GET_USER,
                result -> {
                    mUserModel = (UserModel) result;
                    mBinding.setUserModel(mUserModel);
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

            new DatabaseAsync<AlbumModel>(mContext,
                    mDatabase,
                    mAlbumModel,
                    Constants.Database.UPDATE_ALBUM,
                    result -> mDetailView.onAlbumUpdate()
            ).execute();

        }
    }


    /**
     * Name : deleteAlbumModel
     * <br> Purpose : To delete AlbumModel Data
     */

    public void deleteAlbumData() {
        //To delete AlbumData
        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                mAlbumModel,
                Constants.Database.DELETE_ALBUM,
                result -> mDetailView.onAlbumDelete()
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
                    result -> mDetailView.onUserUpdate()
            ).execute();
        }

    }


    /**
     * Name : deleteUserModel
     * <br> Purpose : To delete UserModel Data
     */
    public void deleteUserModel() {
        //To delete UserData
        new DatabaseAsync<AlbumModel>(mContext,
                mDatabase,
                mUserModel,
                Constants.Database.DELETE_USER,
                result -> mDetailView.onUserDelete()
        ).execute();
    }


}

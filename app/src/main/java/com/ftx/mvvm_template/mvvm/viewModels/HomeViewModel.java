package com.ftx.mvvm_template.mvvm.viewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.framework.model.ApiResponse;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.model.repo.HomeRepository;
import com.ftx.mvvm_template.model.repo.RepositoryImpl;
import com.ftx.mvvm_template.model.rest.ItemDataSourceFactory;
import com.ftx.mvvm_template.mvvm.views.HomeView;
import com.ftx.mvvm_template.mvvm.views.MvvmView;
import com.ftx.mvvm_template.utils.Constants;
import com.ftx.mvvm_template.utils.DatabaseAsync;
import com.ftx.mvvm_template.utils.network.NetworkUtils;

/**
 * Name : HomeViewModel
 * <br> Purpose :  The HomeViewModel extends ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
 * The ViewModel class allows data to survive configuration changes such as screen rotations.
 */

public class HomeViewModel extends BaseViewModel {

    public static final String TAG = BaseViewModel.class.getName();
    public Context mContext;
    public LiveData<PagedList<AlbumModel>> pagedAlbumList;
    public LiveData<PagedList<UserModel>> pagedUserList;
    private MyDatabase mDatabase;
    private MediatorLiveData<ApiResponse> mAlbumLiveData;
    private MediatorLiveData<ApiResponse> mUserLiveData;
    private HomeRepository mHomeRepo;
    private HomeView mHomeView;

    // No argument constructor
    public HomeViewModel() {

    }

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        this.mContext = mContext;
        this.mHomeView = (HomeView) mvpView;
        mDatabase = TemplateApplication.getAppInstance().getDbInstance();
        mAlbumLiveData = new MediatorLiveData<>();
        mUserLiveData = new MediatorLiveData<>();
        mHomeRepo = new RepositoryImpl();


        mAlbumLiveData.observe((LifecycleOwner) this.mContext, apiResponse -> {
        });
        mUserLiveData.observe((LifecycleOwner) this.mContext, apiResponse -> {
        });
        pagedAlbumList = new LivePagedListBuilder(mDatabase.albumDao().getPagedAlbumList(), /* page size */ 10).build();

        //Building the paged list
        pagedUserList = new LivePagedListBuilder(new ItemDataSourceFactory(),
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(10).build())
                .build();


        return super.inIt(mContext, mvpView);
    }

    /**
     * Name : getPagedAlbumList
     * <br> Purpose : To get updated AlbumModel called from HomeFragemnt
     *
     * @return this method will return Livedata of PagedList<AlbumModel>
     */
    public LiveData<PagedList<AlbumModel>> getPagedAlbumList() {
        return pagedAlbumList;
    }


    /**
     * Name : loadAlbumResponse
     * <br> Purpose : This method will be called to get the Album list from API
     *
     * @return
     */
    public void loadAlbumResponse() {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            mHomeView.noInternetConnection(() -> loadAlbumResponse());
        } else {
            mHomeView.showLoader(mContext.getString(R.string.message_loader_loading_albums));
            mAlbumLiveData.addSource(
                    mHomeRepo.getAlbumList(), apiResponse -> {
                        mHomeView.hideLoader();
                        new DatabaseAsync<ApiResponse>(mContext,
                                mDatabase,
                                apiResponse,
                                Constants.Database.INSERT_ALBUMLIST,
                                result -> mAlbumLiveData.setValue(apiResponse)
                        ).execute();
                    }
            );
        }
    }

    /**
     * Name : getPagedUserList
     * <br> Purpose : To get updated UserModel called from HomeFragment.
     *
     * @return this method will return Livedata of PagedList<UserModel>
     */
    public LiveData<PagedList<UserModel>> getPagedUserList() {
        return pagedUserList;
    }


    /**
     * Name : loadUserResponse
     * <br> Purpose : This method will be called to get the user list from API
     *
     * @return
     */
    public void loadUserResponse(int pageId) {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            mHomeView.noInternetConnection(() -> loadUserResponse(pageId));
        } else {
            mHomeView.showLoader(mContext.getString(R.string.message_loader_loading_users));
            mUserLiveData.addSource(
                    mHomeRepo.getUserList(pageId), apiResponse -> {
                        mHomeView.hideLoader();
                        new DatabaseAsync<ApiResponse>(mContext,
                                mDatabase,
                                apiResponse,
                                Constants.Database.INSERT_USERLIST,
                                result -> mUserLiveData.setValue(apiResponse)
                        ).execute();
                    });
        }
    }


}

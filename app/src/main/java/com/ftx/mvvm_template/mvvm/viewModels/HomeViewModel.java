package com.ftx.mvvm_template.mvvm.viewModels;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListProvider;
import android.arch.paging.PagedList;
import android.content.Context;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.framework.model.ApiResponse;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.model.repo.HomeRepository;
import com.ftx.mvvm_template.model.repo.RepositoryImpl;
import com.ftx.mvvm_template.model.rest.TDataSource;
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
    TDataSource tDataSource;
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

        pagedAlbumList = mDatabase.albumDao().getPagedAlbumList().create(
                0,          // initial load position
                new PagedList.Config.Builder()
                        .setPageSize(10)
                        .setPrefetchDistance(9)
                        .build());


        pagedUserList = new LivePagedListProvider<Integer, UserModel>() {
            @Override
            protected DataSource<Integer, UserModel> createDataSource() {
                tDataSource = new TDataSource();
                return tDataSource;
            }
        }.create(0,         // initial load position
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(3)
//                                    .setInitialLoadSizeHint(9)
                        .build());


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
    public LiveData<ApiResponse> loadAlbumResponse() {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            mHomeView.noInternetConnection(() -> loadAlbumResponse());
        } else {
            mHomeView.showLoader(mContext.getString(R.string.message_loader_loading_albums));
            mAlbumLiveData.observe((LifecycleOwner) mContext, apiResponse -> mHomeView.hideLoader());
            mAlbumLiveData.addSource(
                    mHomeRepo.getAlbumList(), apiResponse -> new DatabaseAsync<ApiResponse>(mContext,
                            mDatabase,
                            apiResponse,
                            Constants.Database.INSERT_ALBUMLIST,
                            result -> mAlbumLiveData.setValue(apiResponse)
                    ).execute()
            );
        }
        return mAlbumLiveData;
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
     * @return
     */
    public LiveData<ApiResponse> loadUserResponse(int pageId) {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            mHomeView.noInternetConnection(() -> loadUserResponse(pageId));
        } else {
            mHomeView.showLoader(mContext.getString(R.string.message_loader_loading_users));
            mUserLiveData.observe((LifecycleOwner) mContext, apiResponse -> mHomeView.hideLoader());
            mUserLiveData.addSource(
                    mHomeRepo.getUserList(pageId), apiResponse -> new DatabaseAsync<ApiResponse>(mContext,
                            mDatabase,
                            apiResponse,
                            Constants.Database.INSERT_USERLIST,
                            result -> mUserLiveData.setValue(apiResponse)
                    ).execute()
            );
        }
        return mUserLiveData;
    }


}

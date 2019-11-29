package com.sql.projecttemplate.mvvm.viewModels;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.sql.projecttemplate.mvvm.views.MvvmView;

public interface BVM {

    public ViewModel inIt(Context mContext, MvvmView mvpView);
}

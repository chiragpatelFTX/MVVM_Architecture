package com.ftx.mvvm_template.mvvm.viewModels;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ftx.mvvm_template.mvvm.views.MvvmView;

public interface BVM {

    public ViewModel inIt(Context mContext, MvvmView mvpView);
}

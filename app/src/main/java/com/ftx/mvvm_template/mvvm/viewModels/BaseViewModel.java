package com.ftx.mvvm_template.mvvm.viewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.ftx.mvvm_template.mvvm.views.MvvmView;


public class BaseViewModel extends ViewModel implements BVM {

    @Override
    public ViewModel inIt(Context mContext, MvvmView mvpView) {
        return this;
    }
}

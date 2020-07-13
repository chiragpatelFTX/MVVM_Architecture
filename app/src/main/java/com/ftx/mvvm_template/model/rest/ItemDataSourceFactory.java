package com.ftx.mvvm_template.model.rest;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.ftx.mvvm_template.model.db.models.UserModel;

public class ItemDataSourceFactory extends DataSource.Factory {

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, UserModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, UserModel> create() {
        //getting our data source object
        TDataSource itemDataSource = new TDataSource();

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, UserModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
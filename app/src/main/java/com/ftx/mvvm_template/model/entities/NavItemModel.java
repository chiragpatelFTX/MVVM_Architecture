package com.ftx.mvvm_template.model.entities;

import android.os.Bundle;

/**
 * Name : NavItemModel
 *<br> Purpose : set the fragment configuration which we want to setup on our container.
 */
public class NavItemModel {
    private int mResourceId;
    private String mTitle;
    private boolean mIsSelected;
    private Class mFragment;
    private String mTag;
    private Bundle mBundle;


    public NavItemModel(int aResourceId, String aTitle, Class<?> aFragment, String aTag, Bundle mBundleExtras) {
        this.mResourceId = aResourceId;
        this.mTitle = aTitle;
        this.mFragment = aFragment;

        this.mBundle = mBundleExtras;

        if(mBundleExtras == null){
            this.mBundle = new Bundle();
        }

        this.mBundle.putString("fragment_name",aTitle);


        this.mTag = aTag;
    }

   /* public static List<NavItemModel> generateNavItems() {
        List<NavItemModel> navItemModels = new ArrayList<>();
        navItemModels.add(new NavItemModel(R.mipmap.ic_launcher, "User", UserFragment.class, "User"));
        NavItemModel mChangeAssessment = new NavItemModel(R.mipmap.ic_launcher, "Post", PostFragment.class, "Post");
        navItemModels.add(mChangeAssessment);
        return navItemModels;
    }*/



    public int getResourceId() {
        return mResourceId;
    }

    public void setResourceId(int aResourceId) {
        mResourceId = aResourceId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String aTitle) {
        mTitle = aTitle;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean aIsSelected) {
        mIsSelected = aIsSelected;
    }

    public Class getFragment() {
        return mFragment;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String aTag) {
        mTag = aTag;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle aBundle) {
        mBundle = aBundle;
    }

}

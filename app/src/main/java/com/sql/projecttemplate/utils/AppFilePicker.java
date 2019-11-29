package com.sql.projecttemplate.utils;

import android.app.Activity;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;



/**
 * Name : AppFilePicker
 *<br> Purpose :this class is used to open file picker.
 */
public class AppFilePicker {

    public static AppFilePicker mAppFilePicker;
    private FilePicker mFilePicker;

    public static AppFilePicker getInstance() {
        if (mAppFilePicker == null) {
            mAppFilePicker = new AppFilePicker();
        }
        return mAppFilePicker;
    }

    /**
     * Name : AppFilePicker pickFilesSingle
     *<br> Purpose :    Pick single file from file picker.
     * @param aActivity : activity instance
     */
    public void pickFilesSingle(Activity aActivity) {
        mFilePicker = getFilePicker(aActivity);
        mFilePicker.pickFile();
    }

    /**
     * Name : AppFilePicker pickFilesMultiple
     *<br> Purpose : Pick multiple file from file picker.
     * @param aActivity : activity instance.
     */
    public void pickFilesMultiple(Activity aActivity) {
        mFilePicker = getFilePicker(aActivity);
        mFilePicker.allowMultiple();
        mFilePicker.setMimeType("application/pdf");
        mFilePicker.pickFile();
    }

    /**
     * Name : AppFilePicker getFilePicker
     *<br> Purpose : get instance of the file picker for activity to execute further methods.
     * @param aActivity : instance of activity.
     * @return  : Instance of the file picker.
     */
    public FilePicker getFilePicker(Activity aActivity) {
        mFilePicker = new FilePicker(aActivity);
        mFilePicker.setFilePickerCallback((FilePickerCallback) aActivity);
        mFilePicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
        return mFilePicker;
    }

    public FilePicker getmFilePicker() {
        return mFilePicker;
    }

    public void setmFilePicker(FilePicker mFilePicker) {
        this.mFilePicker = mFilePicker;
    }
}

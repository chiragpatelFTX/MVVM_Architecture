package com.kbeanie.multipicker.utils;

import android.util.Log;

import com.kbeanie.multipicker.core.PickerManager;

public class LogUtils {
    public static void d(String tag, String message){
        if(PickerManager.debugglable){
            AppLog.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(PickerManager.debugglable){
            AppLog.e(tag, message);
        }
    }
}

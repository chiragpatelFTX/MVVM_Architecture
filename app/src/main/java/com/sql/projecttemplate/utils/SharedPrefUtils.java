package com.sql.projecttemplate.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sql.projecttemplate.TemplateApplication;


/**
 * Name : SharedPrefUtils
 *<br> Purpose : This class will contains all the methods related to shared preferences.
 */
public class SharedPrefUtils {
    private static SharedPreferences mSharedPreferences;


    public static SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = TemplateApplication.getAppInstance().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    /**
     * Name : SharedPrefUtils getInt
     *<br> Purpose : Get integer value of given key
     * @param aKey  : key for which we want to get value
     * @param aDefaultValue : default value for given key if does not exist.
     * @return  : integer value.
     */
    public static int getInt(String aKey, int aDefaultValue) {
        return getSharedPreferences().getInt(aKey, aDefaultValue);
    }

    /**
     * Name : SharedPrefUtils getString
     *<br> Purpose : Get String value of given key
     * @param aKey  : key for which we want to get value
     * @param aDefaultValue : default value for given key if does not exist.
     * @return  : String value.
     */
    public static String getString(String aKey, String aDefaultValue) {
        return getSharedPreferences().getString(aKey, aDefaultValue);
    }

    /**
     * Name : SharedPrefUtils getLong
     *<br> Purpose : Get long value of given key
     * @param aKey  : key for which we want to get value
     * @param aDefaultValue : default value for given key if does not exist.
     * @return  : long value.
     */
    public static long getLong(String aKey, long aDefaultValue) {
        return getSharedPreferences().getLong(aKey, aDefaultValue);
    }

    /**
     * Name : SharedPrefUtils getBool
     *<br> Purpose : Get boolean value of given key
     * @param aKey  : key for which we want to get value
     * @param aDefaultValue : default value for given key if does not exist.
     * @return  : boolean value.
     */
    public static boolean getBool(String aKey, boolean aDefaultValue) {
        return getSharedPreferences().getBoolean(aKey, aDefaultValue);
    }

    /**
     * Name : SharedPrefUtils setInt
     *<br> Purpose : Get integer value of given key
     * @param aKey  : key for which we want to get value
     * @param aValue : set value for given key
     */
    public static void setInt(String aKey, int aValue) {
        getSharedPreferences().edit().putInt(aKey, aValue).apply();
    }

    /**
     * Name : SharedPrefUtils setString
     *<br> Purpose : Get String value of given key
     * @param aKey  : key for which we want to get value
     * @param aValue : set value for given key
     */
    public static void setString(String aKey, String aValue) {
        getSharedPreferences().edit().putString(aKey, aValue).apply();
    }

    /**
     * Name : SharedPrefUtils setLong
     *<br> Purpose : Get long value of given key
     * @param aKey  : key for which we want to get value
     * @param aValue : set value for given key
     */
    public static void setLong(String aKey, long aValue) {
        getSharedPreferences().edit().putLong(aKey, aValue).apply();
    }

    /**
     * Name : SharedPrefUtils setBool
     *<br> Purpose : Get boolean value of given key
     * @param aKey  : key for which we want to get value
     * @param aValue : set value for given key
     */
    public static void setBool(String aKey, boolean aValue) {
        getSharedPreferences().edit().putBoolean(aKey, aValue).apply();
    }


}

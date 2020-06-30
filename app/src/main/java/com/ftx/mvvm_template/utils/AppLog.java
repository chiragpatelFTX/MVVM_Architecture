package com.ftx.mvvm_template.utils;

import android.util.Log;

import com.ftx.mvvm_template.BuildConfig;


/**
 * Name : AppLog
 * Purpose : This class prints logs in Logcat it will print only when build flavor is Debug
 */
public class AppLog
{

	/**
	 * Flag represents whether to print debug log or not.
	 */

    public static boolean sFlagDebug = BuildConfig.DEBUG;
	/**
	 * Flag represents whether to print info log or not.
	 */
	public static boolean sFlagInfo = BuildConfig.DEBUG;
	/**
	 * Flag represents whether to print error log or not.
	 */
	public static boolean sFlagErr = BuildConfig.DEBUG;

	/**
	 * This method prints debug log.
	 * 
	 * @param tag
	 * @param message
	 */
	public static void d(String tag, String message)
	{
		if (sFlagDebug)
			Log.d(tag, message);
	}

	/**
	 * This method prints info log.
	 * 
	 * @param tag
	 * @param message
	 */
	public static void i(String tag, String message)
	{
		if (sFlagInfo)
			Log.i(tag, message);
	}

	/**
	 * This method prints error log.
	 * 
	 * @param tag
	 * @param message
	 */
	public static void e(String tag, String message)
	{
		if (sFlagErr)
			Log.e(tag, message);
	}
}

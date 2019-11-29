package com.sql.projecttemplate.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author hrdudhat
 */
public class AssetsUtils {
    /**
     * This method is used to Copy the Full Direcotry and Its sub
     * Directory or Files To another Direcotry in SC Card. Or Some
     * where else.
     */
    public static boolean copyDirectoryFromAssetToSDCard(InputStream aInputStream, File afDstFile) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(afDstFile);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[2048];
            int len;
            while ((len = aInputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }
            aInputStream.close();
            fileOutputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is used to get all the list of Files from the asset
     * folder from the given Directory. In this method if Directory is
     * null then List will be from Direct to Asset Folder.
     * <p/>
     * Here to get all the Files from the Assset Root Folder pass the
     * second paramter as NULL.
     *
     * @param aContext  {@link Context} of the Activity or class from wherer it is
     *                  called.
     * @param asDirName Name of the cuurrent Folder. Pass id NUll of Empty String to
     *                  Get the All the Files of the Asset Root Folder.
     */
    public static String[] getAllFileListFromAsset(Context aContext, String asDirName, AssetManager assetManager) {
        /**
         * Here is AssetManager is NULL then simply returns from the method with
         * NULL value.
         */
        if (assetManager == null) {
            return null;
        }

        try {
            if (asDirName == null || asDirName.trim().length() == 0) {
                return assetManager.list("");
            } else {
                return assetManager.list(asDirName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

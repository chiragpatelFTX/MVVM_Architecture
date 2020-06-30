package com.ftx.mvvm_template.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.views.listeners.NetworkRetryCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Hashtable;

/**
 * Purpose : Contains common Utils
 */

public class CommonUtils {
    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    public static void showToast(Context context, String aMessage) {
        Toast.makeText(context, aMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Purpose : alert dialog with message. and title as a app name.
     *
     * @param context : Context object.
     * @param message : message of dialog box.
     * @param title   : title of dialog box.
     */
    public static AlertDialog.Builder setErrorDialog(Context context, String title, String message) {
        if (title == null) {
            title = context.getString(R.string.app_name);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(message)
                .setPositiveButton(R.string.btn_dialog_okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        return alertDialogBuilder;
    }

    public static void setMessageDialog(Context context, String title, String Message) {
        if (title == null) {
            title = context.getString(R.string.app_name);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(Message).setPositiveButton(R.string.btn_dialog_okay,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * Purpose : This method will be executed when we don't have internet. and if we don't have internet then we can
     * retry from this dialog.
     * @param aContext
     * @param aNetworkRetryCallback
     */
    public static void showNetworkDialog(Context aContext, final NetworkRetryCallback aNetworkRetryCallback) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(aContext);
        alertDialogBuilder.setTitle(aContext.getString(R.string.app_name));
        alertDialogBuilder.setMessage(aContext.getString(R.string.error_network_no_internet));
        alertDialogBuilder.setPositiveButton(aContext.getString(R.string.btn_dialog_retry),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        aNetworkRetryCallback.onNetworkRetry();
                    }
                });

        alertDialogBuilder.setNegativeButton(aContext.getString(R.string.btn_dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }


    public static Dialog showProgressDialog(Context mContext, String... message) {
        Dialog progressDialog = new Dialog(mContext, android.R.style.Theme_Holo_Dialog_NoActionBar);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //here we set layout of progress dialog
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_progress_load, null);
        TextView txtLoadingMessage = (TextView) view.findViewById(R.id.tvLoadingMessage);
        if (message != null && message.length > 0) {
            txtLoadingMessage.setText(message[0]);
        } else {
            txtLoadingMessage.setText(mContext.getString(R.string.loader));
        }
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);

        return progressDialog;

    }

    public static void hideProgressDialog(Dialog mDialog) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * Purpose:This method is used to return typeface.
     *
     * @param c         Context
     * @param assetPath it is a just path of a asset font.
     */
    public static Typeface getTypeFace(Context c, String assetPath) {
        synchronized (cache) {
            assetPath = "fonts/" + assetPath;
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }



   /* public static DeviceRegistration getDeviceInfo(String aUserId) {
        PackageInfo pInfo = null;
        DeviceRegistration mDeviceRegistration = null;
        try {
            pInfo = TemplateApplication.getAppInstance().getApplicationContext().getPackageManager().getPackageInfo("com.pharmacare.fatblaster", 0);
            mDeviceRegistration = new DeviceRegistration();
            mDeviceRegistration.setUser(Integer.parseInt(aUserId));
            mDeviceRegistration.setUdid(BaseApplication.getInstance().getDeviceUuidFactory());
            mDeviceRegistration.setAppVersion(pInfo.versionName);
            mDeviceRegistration.setDeviceName(Build.DEVICE);
            mDeviceRegistration.setDeviceOS(System.getProperty("os.version"));
            mDeviceRegistration.setDeviceType("ANDROID");
            mDeviceRegistration.setDeviceToken(SharedPrefUtils.getString(Constants.SharedPrefs.PUSH_TOKEN, ""));
        }catch (Exception e){
            e.printStackTrace();
        }

        return mDeviceRegistration;
    }*/


    /**
     * Purpose : This method is used to export the internal database of the application to SDCARD.
     *
     * @param aContext  : context object
     * @param databaseName  : name of database which we want to export.
     */
    public static void exportDatabse(Context aContext, String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + aContext.getPackageName() + "//databases//" + databaseName + "";
                String backupDBPath = databaseName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }


}

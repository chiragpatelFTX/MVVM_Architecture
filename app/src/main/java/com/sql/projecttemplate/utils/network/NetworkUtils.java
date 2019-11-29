package com.sql.projecttemplate.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sql.projecttemplate.R;

/**
 * Contains utility classes related ti network connections
 *
 * @author hrdudhat
 */

public class NetworkUtils {
    /**
     * This method is used to check internet connectivity of device.
     * This method returns true if connectivity exist else returns
     * false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * To get error message based on http response code
     *
     * @param aContext  app context.
     * @param aHttpCode Http code to get relevant error message
     */
//    public static String getErrorMessageByHttpCode(Context aContext, int aHttpCode) {
//
//        if (aHttpCode == 408) {
//            return aContext.getString(R.string.error_network_connection_timeout);
//        } else if (aHttpCode == 401 || aHttpCode == 407) {
//            return aContext.getString(R.string.error_network_unauthorized);
//        } else if (aHttpCode == 440) {
//            return aContext.getString(R.string.error_network_session_expire);
//        } else if (aHttpCode > 200 && aHttpCode < 500) {
//            // client error
//            return aContext.getString(R.string.error_network_client_error);
//        } else if (aHttpCode == 504 || aHttpCode == 598 || aHttpCode == 599) {
//            return aContext.getString(R.string.error_network_server_timeout);
//        } else if (aHttpCode >= 500) {
//            // server error
//            return aContext.getString(R.string.error_network_server_errors);
//        } else {
//            return aContext.getString(R.string.error_network_client_error);
//        }
//    }
}

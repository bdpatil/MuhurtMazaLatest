package com.muhurtmaza.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sandeep on 11/22/15.
 */

public class ConnectionDetector {
    private static ConnectionDetector mInstance;
    public static ConnectionDetector getInstance() {
        if (mInstance == null)
            mInstance = new ConnectionDetector();

        return mInstance;
    }

    /**
     * Check for network connectivity.Ping host server if it is reachable
     *
     * @param pContext
     * @return
     */
    public boolean checkConnectivity(Context pContext) {
        ConnectivityManager lConnectivityManager = (ConnectivityManager)
                pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo lNetInfo = lConnectivityManager.getActiveNetworkInfo();
        if (lNetInfo != null && lNetInfo.isConnected()) {
            return true;
        }
        return false;
    }

}


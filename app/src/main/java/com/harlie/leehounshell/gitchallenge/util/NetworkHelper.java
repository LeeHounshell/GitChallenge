package com.harlie.leehounshell.gitchallenge.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;

public class NetworkHelper
{
    private final static String TAG = "LEE: <" + NetworkHelper.class.getSimpleName() + ">";

    // check for network connectivity.
    public static boolean isOnline() {
        return ! isNotOnline();
    }

    public static boolean isNotOnline() {
        Context context = GitChallengeApplication.getAppContext();
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return !(networkInfo != null && networkInfo.isConnected());
        }
        return true;
    }

}


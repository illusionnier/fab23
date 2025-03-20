package com.fabryka.apfabryka;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnection {
    public static boolean checkConnection(Context context, String android_id) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            Toast.makeText(context, activeNetworkInfo.getTypeName() + " UID=" + android_id, 0).show();
            if (activeNetworkInfo.getType() == 1 || activeNetworkInfo.getType() == 0) {
                return true;
            }
            return false;
        }
        return false;
    }
}

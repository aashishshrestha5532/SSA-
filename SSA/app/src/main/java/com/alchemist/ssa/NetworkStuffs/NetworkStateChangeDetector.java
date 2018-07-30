package com.alchemist.ssa.NetworkStuffs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ashish Alton on 7/30/2018.
 */

public class NetworkStateChangeDetector extends BroadcastReceiver {

    private NetworkCallBackInterface callBackInterface;

    public NetworkStateChangeDetector(NetworkCallBackInterface callBackInterface) {
        this.callBackInterface=callBackInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!= null && activeNetworkInfo.isConnected()) {
            callBackInterface.networkSateChanged(true);
        } else {
            callBackInterface.networkSateChanged(false);
        }
    }

}

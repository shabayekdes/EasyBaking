package com.shabayekdes.easybaking.connection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.shabayekdes.easybaking.R;

public class ConnectionManager {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void noConnectionAlert(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.no_connection)
                .setCancelable(false)
                .setMessage("You seem to have lost your connection, please connect and try again!")
                .setPositiveButton("Go to Wifi Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);

                        dialog.cancel();
                    }
                })
                .show();
    }
}


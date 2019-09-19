package com.example.mypassenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class ServiceRestarter extends BroadcastReceiver {

    private static final String TAG = "ServiceRestarter";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: Broadcast received");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        Intent serviceIntent = new Intent(context, NewService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        }else{
            context.startService(serviceIntent);
        }
    }
}

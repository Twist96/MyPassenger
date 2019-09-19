package com.example.mypassenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    TextView mTextView;
    Intent mServiceIntent;
    private static final String TAG = "MainActivity";

    int count = 0;

    private NewService newService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: activity created <<<<< Thread id: " + Thread.currentThread().getId());

        mProgressBar = findViewById(R.id.progress_bar);
        mTextView = findViewById(R.id.progress_text);

        mServiceIntent = new Intent(this, NewService.class);

        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();
    }



    public void start(View view) {
        if (!isServiceRunning(NewService.class)){
            startService(mServiceIntent);
        }
    }

    public void stop(View view) {
        stopService(mServiceIntent);
    }

    public void bind(View view) {
        if(serviceConnection == null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    NewService.NewServiceBinder serviceBinder = (NewService.NewServiceBinder) iBinder;
                    newService = serviceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }

        bindService(mServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind(View view) {
        if (isServiceBound){
            unbindService(serviceConnection);
        }
    }

    public void getRandomNumber(View view) {
        if (isServiceBound){
            mTextView.setText("Random Number: " + newService.getRandomNumber());
        }else{
            mTextView.setText("service not bound");
        }
    }

    public boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())){
                Log.i(TAG, "isServiceRunning: Service is running");
                return true;
            }
        }
        Log.i(TAG, "isServiceRunning: Service is not running");
        return false;
    }
}

package com.example.mypassenger;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private MyBinder mBinder = new MyBinder();
    private Handler mHandler;
    private int mProgress, mMaxValue;
    private Boolean mIsPaused;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mProgress = 0;
        mMaxValue = 50000;
        mIsPaused = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder{

        MyService getService(){
            return MyService.this;
        }

    }


    public void startPretendLongRunningTask(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mProgress >= mMaxValue || mIsPaused){
                    Log.d(TAG, "run: removing callbacks.");
                    mHandler.removeCallbacks(this);
                    pausePretendLongRunningTask();
                }else{
                    Log.d(TAG, "run: update the progress");
                    mProgress += 10;
                    mHandler.postDelayed(this, 100);
                }
            }
        };
    }

    public void pausePretendLongRunningTask(){
        mIsPaused = true;
    }

    private void unpausePretendLongRunningTask(){
        mIsPaused = true;
        startPretendLongRunningTask();
    }

    private Boolean getmIsPaused(){
        return mIsPaused;
    }

    private int getmMaxValue(){
        return mMaxValue;

    }

    public void resetTask(){
        mProgress = 0;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}

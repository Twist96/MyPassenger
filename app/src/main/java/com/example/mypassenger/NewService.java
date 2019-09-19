package com.example.mypassenger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NewService extends Service {

    private static final String TAG = "NewService";
    private int randomNumber;
    private boolean isRandomGeneratorOn;
    private final int min = 0;
    private final int max = 100;
    private int count = 0;

    public static boolean isServiceRuning;

    IBinder binder = new NewServiceBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: service have start <<<<< thread id: " + Thread.currentThread().getId());
        isRandomGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: service destroyed <<<<<");
        stopRandomGeneratedNumber();
        broadcastServiceRestart();
        stopSelf();
        super.onDestroy();
    }

    private void startRandomNumberGenerator(){
        while (isRandomGeneratorOn){
            try{
                Thread.sleep(1000);
                if(isRandomGeneratorOn){
                    randomNumber = new Random().nextInt(max) + min;
                    Log.i(TAG, "startRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + ", Random Number: " + randomNumber);
                    makePhoneRing();
                }
            }catch (Exception e){
                Log.d(TAG, "startRandomNumberGenerator: Thread interrupted");
            }
        }
    }

    private void stopRandomGeneratedNumber(){
        isRandomGeneratorOn = false;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    private void makePhoneRing(){
        count++;
        if (count == 5) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
    }

    private void broadcastServiceRestart(){
        Intent intent = new Intent();
        intent.setAction("restart_service");
        intent.setClass(this, ServiceRestarter.class);
        this.sendBroadcast(intent);
    }

    private void startActivty(){
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    class NewServiceBinder extends Binder{
        public NewService getService(){
            return NewService.this;
        }
    }

}

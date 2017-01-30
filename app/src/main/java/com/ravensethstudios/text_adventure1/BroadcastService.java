package com.ravensethstudios.text_adventure1;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class BroadcastService extends Service {

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "Text-Adventure1.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;




    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                notB();
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    public void notB(){
        NotificationCompat.Builder mBuilder =
            (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setContentTitle("My notification")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, BroadcastService.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }

}
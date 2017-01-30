package com.ravensethstudios.text_adventure1;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BroadcastService extends Service {

    public BroadcastService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent myIntent = new Intent("mybroadcast");
        sendBroadcast(myIntent);
        return START_NOT_STICKY;
    }
}
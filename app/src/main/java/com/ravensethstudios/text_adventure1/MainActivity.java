package com.ravensethstudios.text_adventure1;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView tx;
    int ticker;
    final Handler handler = new Handler();
    public static final String prefs = "prefs";
    boolean running = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx = (TextView) findViewById(R.id.textView);
        SharedPreferences prefs = getSharedPreferences(
                "prefs", Context.MODE_PRIVATE);
        ticker = prefs.getInt("ticker",0);
        tx.setText(prefs.getString("text","Empty")+"\n");


    }
    @Override
        protected void onStart()
        {
            textReader();
            super.onStart();
        }
    @Override
    protected void onPause()
    {
        running = false;
        Log.i("stuff","paused");
        SharedPreferences prefs = getSharedPreferences(
                "prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("ticker",ticker).apply();
        String temp = tx.getText().toString();
        temp = temp.substring(0,temp.length()-1);
        prefs.edit().putString("text",temp).apply();
        super.onPause();
    }
    @Override
    protected void onResume()
    {
        running = true;
        Log.i("stuff","resumed");
        super.onResume();
    }

    public void updateGUI()
    {
        String newText = "Line Number "+ticker+"\n";
        tx.setText(tx.getText()+newText);
    }

    public void textReader()
    {
            boolean x = running;
            Runnable runs = new Runnable() {
                @Override
                public void run() {
                    if (ticker < 100 && running) {
                        ticker++;
                        updateGUI();
                        Log.i("Handler", "Still Running");
                        handler.postDelayed(this, 3000);
                    }
                    else {
                        Log.i("Handler","Stopped");
                        handler.removeCallbacks(this);
                    }
                }

            };
                handler.postDelayed(runs, 3000);



    }
}

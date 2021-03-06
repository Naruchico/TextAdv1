package com.ravensethstudios.text_adventure1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tx;
    Button c1;
    Button c2;
    ImageButton menu;
    int lineNumber;
    int choice = 0;
    final Handler handler = new Handler();
    public static final String prefs = "prefs";
    boolean running = true;
    boolean choiceMade = true;
    Map<Integer, String> text = new LinkedHashMap<Integer, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx = (TextView) findViewById(R.id.textView);
        c1 = (Button) findViewById(R.id.Choice1);
        c2 = (Button) findViewById(R.id.Choice2);
        menu = (ImageButton) findViewById(R.id.menuButton);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        menu.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        lineNumber = prefs.getInt("lineNumber", 0);
        tx.setText(prefs.getString("text", tx.getText()+"") + "\n");
        choice = prefs.getInt("choices",1);
        choiceMade = prefs.getBoolean("choiceMade",true);
        running = prefs.getBoolean("Running",true);

        buildText();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Choice1:
                choiceMade = true;
                c1.setVisibility(v.INVISIBLE);
                c2.setVisibility(v.INVISIBLE);
                tx.setText(tx.getText()+">>>>>"+c1.getText()+"<<<<<<\n");
                lineNumber = choice * 100;
                choice=choice + 2;
                Log.i("Choice run","Run? "+ running);
                setAlarm("test","awesome info!");
                break;
            case R.id.Choice2:
                choiceMade= true;
                c1.setVisibility(v.INVISIBLE);
                c2.setVisibility(v.INVISIBLE);
                tx.setText(tx.getText()+">>>>>"+c2.getText()+" <<<<<<\n");
                lineNumber = (choice + 1) * 100;
                choice = choice + 2;
                startActivity(new Intent(MainActivity.this,BusyActivity.class));
                break;
            case R.id.menuButton:
                startActivity(new Intent(MainActivity.this,MenuActivity.class));
                break;
        }

    }

    @Override
        protected void onStart()
        {
            handler.postDelayed(checker, 1000);

            super.onStart();
        }
    @Override
    protected void onPause()
    {
        running = false;
        Log.i("stuff","paused");
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("lineNumber",lineNumber).apply();
        prefs.edit().putBoolean("Running",running).apply();
        prefs.edit().putBoolean("ChoiceMade",choiceMade).apply();
        prefs.edit().putInt("choices",choice).apply();
        String temp = tx.getText().toString();
        temp = temp.substring(0,temp.length()-1);
        prefs.edit().putString("text",temp).apply();
        super.onPause();
    }
    @Override
    protected void onResume()
    {
        running = true;
        Log.i("stuff","resumed: "+running);
        super.onResume();
    }

    public void updateGUI() {
            if(text.get(lineNumber) != null)
            {
            String newText = text.get(lineNumber)+"\n";
            tx.setText(tx.getText() + newText);
            }
        else if (text.get(lineNumber) == null)
            {
                choiceMade = false;
                buttonBuilder();
            }
    }

    public Runnable checker = new Runnable() {
        @Override
        public void run() {
            if(running && choiceMade)
            {
                lineNumber++;
                updateGUI();
                handler.postDelayed(this,3000);
                Log.i("Checker","all good!");
            }
            else{
                handler.postDelayed(this,3000);
                Log.i("Checker","Stopped");
            }

        }
    };



    public void buildText()
        {
            AssetManager asm = getAssets();
            try {
                InputStream is = asm.open("GameText.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String s;
                while((s = reader.readLine()) != null)
                    {
                        String[] temp = s.split(":");
                        int i = Integer.parseInt(temp[0]);
                        text.put(i,temp[1]);
                    }

            }
            catch(IOException e)
            {
                Log.i("File","File issues!");
            }

        }

    public void buttonBuilder() {
        int btn = choice * 100;
        if(text.get(btn) != null)
            {
                c1.setText(text.get(btn));
                c2.setText(text.get(btn + 100));
                c1.setVisibility(findViewById(R.id.Choice1 ).VISIBLE);
                c2.setVisibility(findViewById(R.id.Choice2).VISIBLE);
            }
        else
        {
            running = false;
            choice = 1000;
            tx.setText(tx.getText() + "Game over!\n");
        }
    }

    public void setAlarm(String title, String msg) {

        // Define a time value of 5 seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis()+10*1000;

        // Define our intention of executing AlertReceiver
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        alertIntent.putExtra("Title",title);
        alertIntent.putExtra("Message",msg);

        // Allows you to schedule for your application to do something at a later date
        // even if it is in he background or isn't active
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // set() schedules an alarm to trigger
        // Trigger for alertIntent to fire in 5 seconds
        // FLAG_UPDATE_CURRENT : Update the Intent if active
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

    }


}

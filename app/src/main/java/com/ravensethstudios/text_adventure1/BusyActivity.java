package com.ravensethstudios.text_adventure1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.webkit.WebView;



public class BusyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busyactivity);

            WebView view = (WebView) findViewById(R.id.webView);
            view.loadUrl("file:///android_asset/clock.gif");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;


        getWindow().setLayout((int) (width*.8),(int)(height*0.6));

    }
    @Override
    public void onBackPressed() {

    }
}

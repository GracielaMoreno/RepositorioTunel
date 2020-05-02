package com.example.cheli.tunelapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Temp_list extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_temp_list);
    }
}

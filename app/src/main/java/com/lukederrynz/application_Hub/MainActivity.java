package com.lukederrynz.application_Hub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log; //Used to generate log messages
import android.view.View;

import com.lukederrynz.android_test.R;

public class MainActivity extends AppCompatActivity {

    String entryMsg = "Android : ";
    public static boolean DEBUG = true;

    // region Android On Events

//    // Called when the activity is first created.
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(entryMsg, "onCreate()");
    }
//
//    // Called when the activity is about to become visible.
//    @Override protected void onStart() {
//        super.onStart();
//        if (DEBUG) Log.d(entryMsg, "onStart()");
//    }
//
//    // Called when the activity has become visible.
//    @Override protected void onResume() {
//        super.onResume();
//        if (DEBUG) Log.d(entryMsg, "onResume()");
//    }
//
//    // Called when another activity takes focus.
//    @Override protected void onPause() {
//        super.onPause();
//        if (DEBUG) Log.d(entryMsg, "onPause");
//    }
//
//    // Called when activity is no longer visible.
//    @Override protected void onStop() {
//        super.onStop();
//        if (DEBUG) Log.d(entryMsg, "onStop()");
//    }
//
//    // Called just before activity is destroyed.
//    @Override protected void onDestroy() {
//        super.onDestroy();
//        if (DEBUG) Log.d(entryMsg, "onDestroy()");
//    }

    //endregion

    // Start the Text To Speech Activity
    public void startActivityTTS(View view) {
        Intent i = new Intent(this, TextToSpeechActivity.class);
        startActivity(i);
    }

    // Start the Weather Activity
    public void startActivityWeather(View view) {
        Intent i = new Intent(this, WeatherActivity.class);
        startActivity(i);
    }

    // Start the Calculator Activity
    public void startActivityCalculator(View view) {
        Intent i = new Intent(this, CalculatorActivity.class);
        startActivity(i);
    }
}

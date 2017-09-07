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

//    // Called when the activity is first created.
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(entryMsg, "onCreate()");
    }

    // Start the Text To Speech Activity
    public void startActivityTTS(View view) {
        Intent i = new Intent(this, TextToSpeechActivity.class);
        startActivity(i);
    }

    //region START ACTIVITY METHODS

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

    // Start the About Activity
    public void startActivityAbout(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    // Start the Dropper Activity
    public void startActivityDropper(View view) {
        Intent i = new Intent(this, DropperActivity.class);
        startActivity(i);
    }

    //endregion

}

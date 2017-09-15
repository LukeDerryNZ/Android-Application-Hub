package com.lukederrynz.application_Hub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log; //Used to generate log messages
import android.view.View;

import com.lukederrynz.android_test.R;


/**
 * Created by Luke Derry on 1/09/2017
 * The Entry Point to my application.
 * Consists of a collection of Activity Start Methods.
 *
 */
public class MainActivity extends AppCompatActivity {

    String entryMsg = "Android : ";
    public static boolean DEBUG = true;


    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState - Bundle : Our saved state for this instance
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(entryMsg, "onCreate()");
    }


    //region START ACTIVITY METHODS
    /**
     * Start the Text To Speech Activity.
     *
     * @param view - View : This view
     */
    public void startActivityTTS(View view) {
        Intent i = new Intent(this, TextToSpeechActivity.class);
        startActivity(i);
    }


    /**
     * Start the Weather Activity.
     *
     * @param view - View : This view
     */
    public void startActivityWeather(View view) {
        Intent i = new Intent(this, WeatherActivity.class);
        startActivity(i);
    }


    /**
     * Start the Calculator Activity.
     *
     * @param view - View : This view
     */
    public void startActivityCalculator(View view) {
        Intent i = new Intent(this, CalculatorActivity.class);
        startActivity(i);
    }


    /**
     * Start the About Activity.
     *
     * @param view - View : This view
     */
    public void startActivityAbout(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }


    /**
     * Start the Dropper Activity.
     *
     * @param view - View : This view
     */
    public void startActivityDropper(View view) {
        Intent i = new Intent(this, DropperActivity.class);
        startActivity(i);
    }
    //endregion

}

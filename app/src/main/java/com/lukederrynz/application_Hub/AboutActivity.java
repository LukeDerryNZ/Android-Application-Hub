package com.lukederrynz.application_Hub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lukederrynz.android_test.R;


/**
 * Created by Luke Derry on 1/09/2017
 * Provides links to my github and linked in profiles.
 * Shameless self-plug ftw!
 *
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initializeViews();
    }


    /**
     * Initialize buttons and url links.
     * Sets up onClick listeners for buttons.
     *
     */
    private void initializeViews() {
        Button gitHub = (Button)findViewById(R.id.About_button_GitHub);
        Button linkedIn = (Button)findViewById(R.id.About_button_LinkedIn);

        gitHub.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                openURL("https://github.com/LukeDerryNZ/Android-Application-Hub");
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                openURL("https://www.linkedin.com/in/lukederry/");
            }
        });
    }


    /**
     * Wrapper function for opening a url and starting it's intent.
     *
     * @param s - String : The URL
     */
    private void openURL(String s) {
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}

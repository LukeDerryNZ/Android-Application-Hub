package com.lukederrynz.android_test;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity {

    private EditText editText;
    private TextToSpeech TTS;
    private Button button_Speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        editText = (EditText)findViewById(R.id.TTS_editText);
        button_Speak = (Button)findViewById(R.id.TTS_button_Speak);
        TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.UK);
                }
            }
        });

        button_Speak.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String toSpeak = editText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    // Called when another activity takes focus.
    @Override public void onPause() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
        super.onPause();
    }

}

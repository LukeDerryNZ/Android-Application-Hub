package com.lukederrynz.android_test;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity {

    private EditText editText;
    private TextToSpeech TTS;
    private Button button_Speak;
    private SeekBar seekBar_Pitch, seekBar_SpeechRate;
    private float pitch = 0.0f, speechRate = 0.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        initializeControls();


        TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.UK);
                }
            }
        });

        // Speak Listener
        button_Speak.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String toSpeak = editText.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();

                TTS.setPitch(pitch);
                TTS.setSpeechRate(speechRate);

                if (!TTS.isSpeaking()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });

        // Pitch Listener
        seekBar_Pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Divide by 10 to get speech rate between 0.0-1.0
                pitch = ((float)i + 1) / 10.0f;
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Speech Rate Listener
        seekBar_SpeechRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speechRate = ((float)i + 1) / 5.0f;
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    // Init UI
    private void initializeControls() {
        editText = (EditText)findViewById(R.id.TTS_editText);
        button_Speak = (Button)findViewById(R.id.TTS_button_Speak);
        seekBar_Pitch = (SeekBar)findViewById(R.id.seekBar_Pitch);
        seekBar_SpeechRate = (SeekBar)findViewById(R.id.seekBar_SpeechRate);
    }

    // Called when another activity takes focus.
    @Override public void onPause() {
        shutDownTTS();
        super.onPause();
    }

    // Called just before activity is destroyed.
    @Override public void onDestroy() {
        shutDownTTS();
        super.onDestroy();
    }

    // Shut down text to speech engine
    private void shutDownTTS() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
    }

}

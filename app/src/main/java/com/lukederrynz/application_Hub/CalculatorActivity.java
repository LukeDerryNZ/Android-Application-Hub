package com.lukederrynz.application_Hub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lukederrynz.android_test.R;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        initializeControls();
    }

    // Custom String Param Listener
    private class myListener implements View.OnClickListener {
        String value;
        private myListener(String s) { value = s; }
        @Override public void onClick(View v) {}
    }

    //
    private void initializeControls() {

        textView = (TextView)findViewById(R.id.Calculator_textView);
        textView.setText("");

        GridLayout numGridLayout = (GridLayout)findViewById(R.id.Calculator_numbersGridLayout);

        // Create number button events
        for(int i=0; i<numGridLayout.getChildCount(); i++) {

            Button tempButton = (Button)numGridLayout.getChildAt(i);
            // create new button event for this button
            numGridLayout.getChildAt(i).setOnClickListener(new myListener(tempButton.getText().toString()) {
                @Override public void onClick(View v) {
                    switch (this.value) {
                        case "=":
                            calculateEquation();
                            break;
                        default:
                            //Toast.makeText(CalculatorActivity.this, "Number "+this.value, Toast.LENGTH_SHORT).show();
                            enterNumber(this.value);
                            break;
                    }
                }
            });
        }


    }

    // Enter number into textView
    private void enterNumber(String numStr) {
        textView.append(numStr);
    }

    // Calculate equation
    private void calculateEquation() {
        Toast.makeText(CalculatorActivity.this, "Calculating Equals...", Toast.LENGTH_SHORT).show();
    }

}

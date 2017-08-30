package com.lukederrynz.application_Hub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lukederrynz.android_test.R;

public class CalculatorActivity extends AppCompatActivity {

    private TextView textView;

    private Double tempAnswer;

    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calculator = new Calculator();

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

        // Create number button events
        GridLayout numGridLayout = (GridLayout)findViewById(R.id.Calculator_numbersGridLayout);
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
                            Toast.makeText(CalculatorActivity.this, "Number "+this.value, Toast.LENGTH_SHORT).show();
                            enterNumber(this.value);
                            break;
                    }
                }
            });
        }

        // Create operator button events
        GridLayout operatorGridLayout = (GridLayout)findViewById(R.id.Calculator_operatorsGridLayout);
        for(int i=0; i<operatorGridLayout.getChildCount(); i++) {
            Button tempButton = (Button)operatorGridLayout.getChildAt(i);
            // Create a new event
            operatorGridLayout.getChildAt(i).setOnClickListener(new myListener(tempButton.getText().toString()) {
                @Override public void onClick(View v) {

                    // Check textView is not empty
                    if (!isTextViewEmpty()) {

                        // Save current textView value
                        tempAnswer = getValueAsDouble();
                        // And clear
                        cleartextView();

                        double result;
                        Calculator.MathOperation<Double> command = null;

                        // Call correct operator event
                        switch (this.value) {

                            case "X":
                                if (command == null) command = calculator.new MultiplyOperation();
                                break;
                            case "+":
                                if (command == null) command = calculator.new AddOperation();
                                break;
                            case "-":
                                if (command == null) command = calculator.new SubtractOperation();
                                break;
                            case "/":
                                if (command == null) command = calculator.new DivideOperation();
                                break;
                            default:
                                Toast.makeText(CalculatorActivity.this, "ERROR: Operator Not Recognized", Toast.LENGTH_SHORT).show();
                        }
                        // and calculate result

                        /*
                            Here I need to append to a list or stack perhaps.
                            We want [ 1stVal 1stOp 2ndVal 2ndOp ]
                            At each Operator, append textVal to list.
                            Then append operator.
                            2         +       2                  *                     4            =
                                 store 2,+               calc(2+2),store 4
                                 count 2                count 3 so calc sum
                                                         remove index[0],[1]
                         */
                        result = command.execute(tempAnswer, getValueAsDouble());

                        Toast.makeText(CalculatorActivity.this, String.valueOf(result), Toast.LENGTH_SHORT).show();

                        // Set textView
                        textView.setText(String.valueOf(result));
                    }

                }
            });
        }


    }

    // Parse textView String as Double and return
    private double getValueAsDouble() {
        return Double.parseDouble(textView.getText().toString());
    }

    // Check null textView String. Return true if empty
    private boolean isTextViewEmpty() {
        return textView.getText().toString().isEmpty();
    }

    // Clear textView
    private void cleartextView() {
        textView.setText("");
    }

    // Clear All
    private void clearAll() {
        cleartextView();
        tempAnswer = 0.0;
    }

    // Enter number into textView
    private void enterNumber(String numStr) {
        textView.append(numStr);
    }

    // Calculate equation
    private void calculateEquation() {

        // Get number in memory


        Toast.makeText(CalculatorActivity.this, "Calculating Equals...", Toast.LENGTH_SHORT).show();
    }

}

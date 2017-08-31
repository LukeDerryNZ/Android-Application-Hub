package com.lukederrynz.application_Hub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lukederrynz.android_test.R;

import java.util.ArrayDeque;
import java.util.Deque;

public class CalculatorActivity extends AppCompatActivity {

    private TextView textView;
    private Calculator calculator;
    private Deque<String> equationStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calculator = new Calculator();
        equationStack = new ArrayDeque<>();

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
            // Cache button
            Button tempButton = (Button)numGridLayout.getChildAt(i);
            // Create new listener passing string as parameter
            tempButton.setOnClickListener(new myListener(tempButton.getText().toString()) {
                @Override public void onClick(View v) {
                    if (this.value.equals("=")) {
                        switch (equationStack.size()) {
                            case 2:
                                // If textView is not empty
                                if (!isTextViewEmpty()) {
                                    equationStack.push(getValueAsString());
                                }
                            case 3:
                                textView.setText(String.valueOf(calcEquation()));
                                equationStack.clear();
                        }
                    } else {
                        enterNumber(this.value);
                    }
                }
            });
        }

        // Create operator button events
        GridLayout operatorGridLayout = (GridLayout)findViewById(R.id.Calculator_operatorsGridLayout);
        for(int i=0; i<operatorGridLayout.getChildCount(); i++) {
            final Button tempButton = (Button)operatorGridLayout.getChildAt(i);
            // Create a new event
            operatorGridLayout.getChildAt(i).setOnClickListener(new myListener(tempButton.getText().toString()) {
                @Override public void onClick(View v) {

                    // Bail if textView is empty - Note that this disallows negatives
                    if (isTextViewEmpty()) return;

                    switch (equationStack.size()) {
                        case 0:
                            // Push textView val
                            equationStack.push(getValueAsString());
                            // And operator
                            equationStack.push(this.value);
                            cleartextView();
                            break;

                        case 1:
                            Toast.makeText(CalculatorActivity.this, "ERROR:STACK SIZE=1", Toast.LENGTH_SHORT).show();
                            break;

                        case 2:
                            // If peek is operator, replace operator - this resolves multiple operators issue
                            if ( isOperator(equationStack.peek()) ) {
                                equationStack.pop();
                                equationStack.push(this.value);
                            }

                            // Push value to stack
                            equationStack.push(getValueAsString());

                            // And clear textView
                            cleartextView();

                            // Proceed to case 3

                        case 3:
                            // Process the first equation
                            double v1 = Double.valueOf(equationStack.pop());
                            String op = equationStack.pop();
                            double v2 = Double.valueOf(equationStack.pop());

                            // Calculate equation and push to stack
                            switch(op) {
                                case "+":
                                    equationStack.push( String.valueOf(calculator.new AddOperation().execute(v1, v2)));
                                    break;
                                case "-":
                                    equationStack.push( String.valueOf(calculator.new SubtractOperation().execute(v1, v2)));
                                    break;
                                case "X":
                                    equationStack.push( String.valueOf(calculator.new MultiplyOperation().execute(v1, v2)));
                                    break;
                                case "/":
                                    equationStack.push( String.valueOf(calculator.new DivideOperation().execute(v1, v2)));
                                    break;
                                default:
                                    Toast.makeText(CalculatorActivity.this, "Invalid Operator", Toast.LENGTH_SHORT).show();
                                    return;
                            }
                            // Finally add operator
                            equationStack.push(this.value);
                    }

                }
            });
        }


    }

    // Calculate valueOne Operator valueTwo
    // Note that NO other equation format is compatible
    private double calcEquation() {

        cleartextView();
        // Process the first equation
        double v1 = Double.valueOf(equationStack.pop());
        String op = equationStack.pop();
        double v2 = Double.valueOf(equationStack.pop());


        // Calculate equation and push to stack
        switch(op) {
            case "+":
                return calculator.new AddOperation().execute(v1, v2);
            case "-":
                return calculator.new SubtractOperation().execute(v1, v2);
            case "X":
                return calculator.new MultiplyOperation().execute(v1, v2);
            case "/":
                return calculator.new DivideOperation().execute(v1, v2);
            default:
                Toast.makeText(CalculatorActivity.this, "Invalid Operator", Toast.LENGTH_SHORT).show();
                break;
        }
        // TODO //
        return v1; // ERROR - Just return v1 for now

    }

    private String getValueAsString() {
        return textView.getText().toString();
    }

    private boolean isOperator(String s) {
        return (s.equals("=") || s.equals("+") || s.equals("X") || s.equals("/"));
    }

    // Check null textView String. Return true if empty
    private boolean isTextViewEmpty() {
        return textView.getText().toString().isEmpty();
    }

    // Clear textView
    private void cleartextView() {
        textView.setText("");
    }

    // Enter number into textView
    private void enterNumber(String numStr) {
        textView.append(numStr);
    }

}

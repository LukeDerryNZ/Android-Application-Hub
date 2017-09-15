package com.lukederrynz.application_Hub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lukederrynz.android_test.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;


/**
 * Created by Luke Derry on ‎Friday, ‎1 ‎September ‎2017
 * Non-Scientific Calculator Application.
 * Supports:
 * - Multiplication/Division/Addition/Subtraction
 * - Clear function
 *
 */
public class CalculatorActivity extends AppCompatActivity {

    private TextView textView;
    private Calculator calculator;

    // Our Equation deque
    private Deque<String> equationDeque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calculator = new Calculator();
        equationDeque = new ArrayDeque<>();

        initializeControls();
    }


    /**
     * Initialize all out UI Elements.
     *
     */
    private void initializeControls() {

        textView = (TextView) findViewById(R.id.Calculator_textView);
        textView.setText("");

        initNumberGrid();
        initOperatorGrid();
    }


    /**
     * Custom String Param Listener.
     * Provide a String param for our onClick Listeners.
     *
     */
    private class myListener implements View.OnClickListener {
        String value;
        private myListener(String s) { value = s; }
        @Override public void onClick(View v) {}
    }


    /**
     * Initialize number grid and listeners.
     *
     */
    private void initNumberGrid() {

        // Create number button events
        // Using android.support.v7.widget.GridLayout
        GridLayout numGridLayout = (GridLayout)findViewById(R.id.Calculator_numbersGridLayout);
        for(int i=0; i<numGridLayout.getChildCount(); i++) {
            // Cache button
            Button tempButton = (Button)numGridLayout.getChildAt(i);
            // Create new listener passing string as parameter
            tempButton.setOnClickListener(new myListener(tempButton.getText().toString()) {
                @Override public void onClick(View v) {
                    if (this.value.equals("=")) {

                        switch (equationDeque.size()) {
                            case 2:
                                // If textView is not empty
                                if (!isTextViewEmpty()) {
                                    equationDeque.push(getValueAsString());
                                }
                            case 3:
                                textView.setText(String.valueOf(calcEquation()));
                                equationDeque.clear();
                        }
                    } else {
                        // Check for decimal
                        if (this.value.equals(".") && textView.getText().toString().contains(".")) {
                            return;
                        }
                        enterNumber(this.value);
                    }
                }
            });
        }
    }


    /**
     * Initialize operator grid and listeners.
     *
     * TODO: Requires IMMENSE refactoring
     */
    private void initOperatorGrid() {
        // Create operator button events
        GridLayout operatorGridLayout = (GridLayout) findViewById(R.id.Calculator_operatorsGridLayout);
        for(int i=0; i<operatorGridLayout.getChildCount(); i++) {
            final Button tempButton = (Button)operatorGridLayout.getChildAt(i);
            // Create a new event
            operatorGridLayout.getChildAt(i).setOnClickListener(new myListener(tempButton.getText().toString()) {
                @Override public void onClick(View v) {

                    // Bail if textView is empty - Note that this disallows negatives
                    if (isTextViewEmpty()) return;

                    // Clear button
                    if (this.value.equals("C")) {
                        clearAll();
                        return;
                    }

                    // Depending on the deque size we follow different logic
                    switch (equationDeque.size()) {
                        case 0:
                            // Push textView val
                            equationDeque.push(getValueAsString());
                            // And operator
                            equationDeque.push(this.value);
                            cleartextView();
                            break;

                        case 1:
                            Toast.makeText(CalculatorActivity.this, "ERROR:DEQUE SIZE=1", Toast.LENGTH_SHORT).show();
                            break;

                        case 2:
                            // If peek is operator, replace operator - this resolves multiple operators issue
                            if ( isOperator(equationDeque.peek()) ) {
                                equationDeque.pop();
                                equationDeque.push(this.value);
                            }

                            // Push value to deque
                            equationDeque.push(getValueAsString());

                            // And clear textView
                            cleartextView();

                            // Proceed to case 3

                        case 3:
                            // Process the first equation
                            double v2 = Double.valueOf(equationDeque.pop());
                            String op = equationDeque.pop();
                            double v1 = Double.valueOf(equationDeque.pop());

                            // Calculate equation and push to deque
                            switch(op) {
                                case "+":
                                    equationDeque.push( String.valueOf(calculator.new AddOperation().execute(v1, v2)));
                                    break;
                                case "-":
                                    equationDeque.push( String.valueOf(calculator.new SubtractOperation().execute(v1, v2)));
                                    break;
                                case "X":
                                    equationDeque.push( String.valueOf(calculator.new MultiplyOperation().execute(v1, v2)));
                                    break;
                                case "/":
                                    equationDeque.push( String.valueOf(calculator.new DivideOperation().execute(v1, v2)));
                                    break;
                                default:
                                    Toast.makeText(CalculatorActivity.this, "Invalid Operator", Toast.LENGTH_SHORT).show();
                                    return;
                            }
                            // Finally add operator
                            equationDeque.push(this.value);
                    }
                }
            });
        }
    }


    /**
     * Calculate (valueOne Operator valueTwo).
     * Note that NO other equation format is compatible.
     *
     * @return - double : The equation result
     */
    private double calcEquation() {

        cleartextView();
        // Process the first equation
        double v2 = Double.valueOf(equationDeque.pop());
        String op = equationDeque.pop();
        double v1 = Double.valueOf(equationDeque.pop());
        double result = 0.0;

        // Calculate equation and push to deque
        switch(op) {
            case "+":
                result = calculator.new AddOperation().execute(v1, v2);
                break;
            case "-":
                result = calculator.new SubtractOperation().execute(v1, v2);
                break;
            case "X":
                result = calculator.new MultiplyOperation().execute(v1, v2);
                break;
            case "/":
                result = calculator.new DivideOperation().execute(v1, v2);
                break;
            default:
                Toast.makeText(CalculatorActivity.this, "Invalid Operator", Toast.LENGTH_SHORT).show();
                break;
        }
        return round(result);
    }


    /**
     * Round the Double to 4DP.
     *
     * @param d - double : Input Double
     * @return - double : The rounded result
     */
    private double round(double d) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        return (Double.valueOf(df.format(d)));
    }


    /**
     * Reads the textView as a string.
     *
     * @return - String : String result
     */
    private String getValueAsString() {
        return textView.getText().toString();
    }


    /**
     * Determines if the input string is one of our operators.
     *
     * @param s - String : Input string
     * @return - boolean : true if s in (=,+,X,/,-)
     */
    private boolean isOperator(String s) {
        return (s.equals("=") || s.equals("+") || s.equals("X") || s.equals("/") || s.equals("-"));
    }


    /**
     * Check null textView String. Return true if empty.
     * Converts to a string first.
     *
     * @return - boolean : True if textView is empty
     */
    private boolean isTextViewEmpty() {
        return textView.getText().toString().isEmpty();
    }


    /**
     * Clears the textView element.
     * Sets it to '""'.
     *
     */
    private void cleartextView() {
        textView.setText("");
    }


    /**
     * Clears the textView and equation deque.
     *
     */
    private void clearAll() {
        cleartextView();
        equationDeque.clear();
    }


    /**
     * Appends the provided string into textView.
     *
     * @param numStr
     */
    private void enterNumber(String numStr) {
        // Sanity check : The param should really be a char
        // TODO: Implement char for operators and numbers
        if (numStr.length() != 1) {
            Toast.makeText(this, "INVALID numStr: CalculatorActivity:enterNumber()", Toast.LENGTH_SHORT).show();
            return;
        }
        textView.append(numStr);
    }

}

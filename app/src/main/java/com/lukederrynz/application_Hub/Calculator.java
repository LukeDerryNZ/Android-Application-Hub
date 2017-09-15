package com.lukederrynz.application_Hub;

/**
 * Created by Luke on 30/08/2017
 *
 * Provides common calculator functions.
 */
class Calculator {


    /**
     * Our operation interface contract.
     * Requires two operators.
     *
     * @param <E>
     */
    interface MathOperation<E> {
        E execute(E op1, E op2);
    }


    /**
     * Additiona operation.
     *
     */
    class AddOperation implements MathOperation<Double> {
        @Override public Double execute(Double op1, Double op2) {
            return (double)op1 + op2;
        }
    }


    /**
     * Subtraction operation.
     *
     */
    class SubtractOperation implements MathOperation<Double> {
        @Override public Double execute(Double op1, Double op2) {
            return (double)op1 - op2;
        }
    }


    /**
     * Multiplication operation.
     *
     */
    class MultiplyOperation implements MathOperation<Double> {
        @Override public Double execute(Double op1, Double op2) {
            return (double)op1 * op2;
        }
    }


    /**
     * Division operation.
     * Accounts for divide by zero error.
     *
     */
    class DivideOperation implements MathOperation<Double> {
        @Override public Double execute(Double op1, Double op2) {
            double temp = 0.0;
            try {
                temp = op1 / op2;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return temp;
        }
    }

}

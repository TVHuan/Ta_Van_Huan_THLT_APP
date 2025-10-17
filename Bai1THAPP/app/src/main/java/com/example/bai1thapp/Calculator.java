package com.example.bai1thapp;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    public interface CalculatorListener {
        void onDisplayChanged(String expression, String result);
        void onError(String message);
        void onHistoryUpdated(List<String> history);
    }

    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewOperation = true;
    private String currentExpression = "";
    private final List<String> history = new ArrayList<>();
    private final CalculatorListener listener;

    public Calculator(CalculatorListener listener) {
        this.listener = listener;
        clear();
    }

    public void inputDigit(String digit) {
        if (isNewOperation) {
            if (operator.isEmpty()) {
                currentExpression = "";
            }
            currentNumber = digit;
            isNewOperation = false;
        } else {
            if (currentNumber.equals("0")) {
                currentNumber = digit;
            } else {
                currentNumber += digit;
            }
        }
        updateDisplay();
    }

    public void inputDot() {
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty() || isNewOperation) {
                currentNumber = "0.";
                isNewOperation = false;
            } else {
                currentNumber += ".";
            }
            updateDisplay();
        }
    }

    public void setOperator(String op) {
        if (isNewOperation && !operator.isEmpty()) {
            operator = op;
            currentExpression = formatNumber(firstNumber) + " " + operator;
            updateDisplay();
            return;
        }

        if (currentNumber.isEmpty()) {
            return;
        }

        if (!operator.isEmpty()) {
            calculate();
        }

        try {
            firstNumber = Double.parseDouble(currentNumber);
        } catch (NumberFormatException e) {
            if (listener != null) listener.onError("Số không hợp lệ");
            return;
        }

        operator = op;
        currentExpression = formatNumber(firstNumber) + " " + operator;
        isNewOperation = true;
        updateDisplay();
    }

    public void delete() {
        if (!currentNumber.isEmpty() && !isNewOperation) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            if (currentNumber.isEmpty()) {
                currentNumber = "0";
                isNewOperation = true;
            }
            updateDisplay();
        }
    }

    public void clear() {
        currentNumber = "";
        operator = "";
        firstNumber = 0;
        isNewOperation = true;
        currentExpression = "";
        if (listener != null) {
            listener.onDisplayChanged("", "0");
        }
    }

    public void calculate() {
        if (operator.isEmpty() || currentNumber.isEmpty() || isNewOperation) {
            return;
        }

        double secondNumber;
        try {
            secondNumber = Double.parseDouble(currentNumber);
        } catch (NumberFormatException e) {
            if (listener != null) listener.onError("Số không hợp lệ");
            clear();
            return;
        }

        if (operator.equals("÷") && secondNumber == 0) {
            if (listener != null) {
                listener.onError("Không thể chia cho 0!");
            }
            clear();
            return;
        }

        double result = 0;
        switch (operator) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "×": result = firstNumber * secondNumber; break;
            case "÷": result = firstNumber / secondNumber; break;
        }

        String fullExpression = formatNumber(firstNumber) + " " + operator + " " + formatNumber(secondNumber) + " = " + formatNumber(result);
        history.add(fullExpression);

        currentExpression = fullExpression;
        currentNumber = formatNumber(result);
        firstNumber = result;
        operator = "";
        isNewOperation = true;

        updateDisplay();

        if (listener != null) {
            listener.onHistoryUpdated(new ArrayList<>(history));
        }
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    private void updateDisplay() {
        if (listener == null) return;

        String resultText;
        if (isNewOperation) {
            if (currentExpression.contains("=")) {
                resultText = currentNumber;
            } else {
                resultText = formatNumber(firstNumber);
            }
        } else {
            resultText = currentNumber;
        }

        if (resultText.isEmpty()) {
            resultText = "0";
        }

        listener.onDisplayChanged(currentExpression, resultText);
    }

    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            return String.valueOf(number);
        }
    }
}

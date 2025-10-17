package com.example.bai1thapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Calculator.CalculatorListener {

    private TextView tvResult, tvExpression;
    private Calculator calculator;

    private List<String> calculationHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);

        calculator = new Calculator(this);

        setupNumberButtons();
        setupOperatorButtons();
        setupFunctionButtons();
    }

    private void setupNumberButtons() {
        View.OnClickListener numberClickListener = v -> {
            Button button = (Button) v;
            calculator.inputDigit(button.getText().toString());
        };
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberClickListener);
        }
        findViewById(R.id.btnDot).setOnClickListener(v -> calculator.inputDot());
    }

    private void setupOperatorButtons() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> calculator.setOperator("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> calculator.setOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> calculator.setOperator("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> calculator.setOperator("÷"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculator.calculate());
    }

    private void setupFunctionButtons() {
        findViewById(R.id.btnClear).setOnClickListener(v -> calculator.clear());
        findViewById(R.id.btnDelete).setOnClickListener(v -> calculator.delete());
        findViewById(R.id.btnHistory).setOnClickListener(v -> showHistoryDialog());
    }


    private void showHistoryDialog() {
        if (calculationHistory.isEmpty()) {
            Toast.makeText(this, "Chưa có lịch sử tính toán", Toast.LENGTH_SHORT).show();
            return;
        }
        CharSequence[] historyItems = calculationHistory.toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lịch sử Phép tính")
                .setItems(historyItems, null) // null listener vì chỉ để hiển thị
                .setPositiveButton("Đóng", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //--- Triển khai các phương thức
    @Override
    public void onDisplayChanged(String expression, String result) {
        tvExpression.setText(expression);
        tvResult.setText(result);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onHistoryUpdated(List<String> history) {
        this.calculationHistory = history;
    }
}


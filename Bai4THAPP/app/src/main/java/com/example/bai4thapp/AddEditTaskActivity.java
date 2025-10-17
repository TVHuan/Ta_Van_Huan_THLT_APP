package com.example.bai4thapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddEditTaskActivity extends AppCompatActivity {
    EditText txtTen, txtMoTa, txtHan;
    Button btnLuu;
    int index = -1;
    ArrayList<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        txtTen = findViewById(R.id.txtTen);
        txtMoTa = findViewById(R.id.txtMoTa);
        txtHan = findViewById(R.id.txtHan);
        btnLuu = findViewById(R.id.btnLuu);
        list = TaskStorage.doc(this);
        Intent intent = getIntent();
        if (intent.hasExtra("task")) {
            Task t = (Task) intent.getSerializableExtra("task");
            index = intent.getIntExtra("index", -1);
            txtTen.setText(t.getTen());
            txtMoTa.setText(t.getMoTa());
            txtHan.setText(t.getHan());
        }
        btnLuu.setOnClickListener(v -> {
            String ten = txtTen.getText().toString();
            String moTa = txtMoTa.getText().toString();
            String han = txtHan.getText().toString();
            if (ten.isEmpty() || han.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            Task t = new Task(ten, moTa, han, false);
            if (index >= 0) list.set(index, t); else list.add(t);
            TaskStorage.luu(this, list);
            MainActivity.datNhac(this, t);
            Toast.makeText(this, "Đã lưu công việc", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

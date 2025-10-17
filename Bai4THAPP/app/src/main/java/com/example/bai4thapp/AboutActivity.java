package com.example.bai4thapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tv = findViewById(R.id.txtThongTin);
        tv.setText("Ứng dụng S-Task\nNgười thực hiện: Huấn Tạ\nLớp: Android Development\nNăm: 2025");
    }
}

package com.example.bai3thapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Spinner spThanhPho;
    EditText edtThanhPho;
    Button btnXem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spThanhPho = findViewById(R.id.spThanhPho);
        edtThanhPho = findViewById(R.id.edtThanhPho);
        btnXem = findViewById(R.id.btnXem);
        String[] ds = {"Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Huế", "Hải Phòng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThanhPho.setAdapter(adapter);
        btnXem.setOnClickListener(v -> {
            String tp = edtThanhPho.getText().toString().trim();
            if (tp.isEmpty()) tp = spThanhPho.getSelectedItem().toString();
            Intent i = new Intent(MainActivity.this, WeatherActivity.class);
            i.putExtra("thanhpho", tp);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Giới thiệu ứng dụng");
            builder.setMessage("Ứng dụng MyWeather\nNgười thực hiện: Huấn Tạ\nMô tả: Hiển thị thời tiết thực tế từ OpenWeatherMap và dữ liệu giả lập khi cần.");
            builder.setPositiveButton("Đóng", (DialogInterface dialog, int which) -> dialog.dismiss());
            builder.show();
        }
        return true;
    }
}

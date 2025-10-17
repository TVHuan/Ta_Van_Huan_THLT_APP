package com.example.bai3thapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeatherActivity extends AppCompatActivity {
    TextView txtTP, txtNhietDo, txtDoAm, txtTrangThai, txtSource;
    ImageView img;
    ProgressBar progressBar;
    Map<String, String[]> duLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        txtTP = findViewById(R.id.txtTP);
        txtNhietDo = findViewById(R.id.txtNhietDo);
        txtDoAm = findViewById(R.id.txtDoAm);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        img = findViewById(R.id.imgWeather);
        progressBar = findViewById(R.id.progressBar);
        txtSource = findViewById(R.id.txtSource);
        duLieu = new HashMap<>();
        duLieu.put("Hà Nội", new String[]{"30°C", "70%", "Nắng", "sunny"});
        duLieu.put("Hồ Chí Minh", new String[]{"32°C", "65%", "Nắng nhẹ", "sunny"});
        duLieu.put("Đà Nẵng", new String[]{"28°C", "80%", "Có mây", "cloudy"});
        duLieu.put("Huế", new String[]{"27°C", "85%", "Mưa", "rainy"});
        duLieu.put("Hải Phòng", new String[]{"29°C", "75%", "Nhiều mây", "cloudy"});

        String tp = getIntent().getStringExtra("thanhpho");
        if (tp == null) tp = "Hà Nội";
        txtTP.setText(tp);
        fetchWeather(tp);
    }

    void fetchWeather(String city) {
        progressBar.setVisibility(View.VISIBLE);
        txtSource.setText("");
        String apiKey = "1ddee0ab018782291d0eddd5236f8b80";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        double temp = main.getDouble("temp");
                        int humidity = main.getInt("humidity");
                        JSONArray weatherArr = response.getJSONArray("weather");
                        JSONObject weather = weatherArr.getJSONObject(0);
                        String desc = weather.getString("description");
                        String mainWeather = weather.getString("main");
                        txtNhietDo.setText("Nhiệt độ: " + Math.round(temp) + "°C");
                        txtDoAm.setText("Độ ẩm: " + humidity + "%");
                        txtTrangThai.setText("Trạng thái: " + capitalize(desc));
                        if (mainWeather.equalsIgnoreCase("Clear")) img.setImageResource(R.drawable.sunny);
                        else if (mainWeather.equalsIgnoreCase("Clouds")) img.setImageResource(R.drawable.cloudy);
                        else img.setImageResource(R.drawable.rainy);
                        txtSource.setText("Nguồn: OpenWeatherMap (thực)");
                    } catch (Exception e) {
                        showFallback(city);
                    } finally {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    showFallback(city);
                });
        queue.add(req);
    }

    void showFallback(String city) {
        String[] t = duLieu.get(city);
        if (t == null) {
            txtNhietDo.setText("Nhiệt độ: -");
            txtDoAm.setText("Độ ẩm: -");
            txtTrangThai.setText("Trạng thái: Không có dữ liệu");
            img.setImageResource(R.drawable.cloudy);
            txtSource.setText("Nguồn: Dữ liệu giả lập");
            return;
        }
        txtNhietDo.setText("Nhiệt độ: " + t[0]);
        txtDoAm.setText("Độ ẩm: " + t[1]);
        txtTrangThai.setText("Trạng thái: " + t[2]);
        if (t[3].equals("sunny")) img.setImageResource(R.drawable.sunny);
        else if (t[3].equals("cloudy")) img.setImageResource(R.drawable.cloudy);
        else img.setImageResource(R.drawable.rainy);
        txtSource.setText("Nguồn: Dữ liệu giả lập");
    }

    String capitalize(String s) {
        if (s == null || s.length() == 0) return s;
        String[] parts = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i];
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
            if (i < parts.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}

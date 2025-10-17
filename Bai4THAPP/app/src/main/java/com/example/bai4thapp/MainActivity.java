package com.example.bai4thapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> list;
    TaskAdapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        list = TaskStorage.doc(this);
        adapter = new TaskAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditTaskActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = TaskStorage.doc(this);
        adapter = new TaskAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) startActivity(new Intent(this, AddEditTaskActivity.class));
        else if (item.getItemId() == R.id.menu_clear) {
            TaskStorage.xoaTatCa(this);
            list.clear();
            adapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.menu_about) startActivity(new Intent(this, AboutActivity.class));
        return true;
    }

    public static void datNhac(Context context, Task task) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(task.getHan());
            if (date == null) return;
            long time = date.getTime();
            if (time < System.currentTimeMillis()) return;
            Intent intent = new Intent(context, ReminderReceiver.class);
            intent.putExtra("ten", task.getTen());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } catch (Exception ignored) {}
    }
}

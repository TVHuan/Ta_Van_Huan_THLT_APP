package com.example.bai4thapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class TaskStorage {
    private static final String PREFS = "TASK_PREFS";
    private static final String KEY = "TASK_LIST";

    public static void luu(Context context, ArrayList<Task> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, new Gson().toJson(list));
        editor.apply();
    }

    public static ArrayList<Task> doc(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY, null);
        if (json == null) return new ArrayList<>();
        return new Gson().fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
    }

    public static void xoaTatCa(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}

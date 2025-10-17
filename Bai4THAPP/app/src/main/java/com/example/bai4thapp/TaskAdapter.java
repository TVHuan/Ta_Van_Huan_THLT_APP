package com.example.bai4thapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private ArrayList<Task> list;

    public TaskAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task t = list.get(position);
        holder.ten.setText(t.getTen());
        holder.han.setText("Hạn: " + t.getHan());
        holder.checkBox.setChecked(t.isHoanThanh());
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, AddEditTaskActivity.class);
            i.putExtra("index", position);
            i.putExtra("task", t);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView ten, han;
        CheckBox checkBox;
        public TaskViewHolder(View itemView) {
            super(itemView);
            ten = itemView.findViewById(R.id.txtTen);
            han = itemView.findViewById(R.id.txtHan);
            checkBox = itemView.findViewById(R.id.chkHoanThanh);
        }
    }
}

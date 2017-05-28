package com.example.android.planning;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    //Items for intern using
    private ArrayList<String> tasks = new ArrayList<>();
    private Context context;
    private String username;

    //Database with Accounts and Tasks tables
    private DatabaseHelper database;

    /**
     * Constructor for custom RecyclerView Adapter
     */
    public RecyclerAdapter (String username, Context context) {
        this.context = context;
        this.username = username;
        database = new DatabaseHelper(context);
        this.tasks = database.getTasks(username);
    }

    /**
     * Update the data which is written in RecyclerView from database
     */
    public void updateData() {
        this.tasks.clear();
        if (database.getTasks(username) != null)
            this.tasks = database.getTasks(username);
        else
            this.tasks = new ArrayList<>();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder =  new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder,final int position) {
        holder.taskTV.setText(tasks.get(position));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Task " + position + " removed successfully", Toast.LENGTH_SHORT).show();
                ((ToDoList)context).removeTask(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * ViewHolder for RecyclerView UI
     */
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTV;
        public Button deleteButton;
        public RecyclerViewHolder(View view) {
            super(view);
            taskTV = (TextView) view.findViewById(R.id.task_content);
            deleteButton = (Button) view.findViewById(R.id.delete_button);
        }
    }
}

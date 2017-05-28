package com.example.android.planning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoList extends Activity{

    //Database with Accounts and Tasks
    private DatabaseHelper database = new DatabaseHelper(ToDoList.this);

    //UI Items
    private String username;
    private Button logoutButton;
    private Button insertButton;
    private TextView welcomeTV;
    private EditText task;

    //Items for RecyclerView Adapter
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerViewAdapter;
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                username = "Friend";
            } else {
                username = extras.getString("Username");
            }
        } else {
            username = "Friend";
        }

        task = (EditText) findViewById(R.id.taskET);
        initiateTheRecyclerView();

        welcomeTV = (TextView) findViewById(R.id.userTV);
        welcomeTV.setText("Welcome, " + username + "! Here is your plan");

        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        insertButton = (Button) findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    /**
     * Add a new task in database and in RecyclerView with the data from EditText
     */
    private void addTask() {
        if (task.getText().toString().equals("")) {
            Toast.makeText(this, "Empty field!", Toast.LENGTH_LONG).show();
        } else {
            database.insertTask(username, task.getText().toString());
            task.setText("");
            hideSoftKeyboard();
            if (recyclerViewAdapter == null)
                initiateTheRecyclerView();
            recyclerViewAdapter.updateData();
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Removes task from the database and update the RecyclerView
     */
    public void removeTask(int position) {
        database.deleteTask(position + 1, username);

        recyclerViewAdapter.updateData();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Initiates the RecyclerView
     */
    private void initiateTheRecyclerView() {
        if (database.getTasks(username) == null) {
            Toast.makeText(this, "You have no tasks for now", Toast.LENGTH_LONG).show();
        } else {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            recyclerViewAdapter = new RecyclerAdapter(username, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    /**
     * Go back to Welcome Activity and destroy this one
     */
    private void logout() {
        welcomeTV.setText(this.getResources().getString(R.string.hello_user));
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
        finish();
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

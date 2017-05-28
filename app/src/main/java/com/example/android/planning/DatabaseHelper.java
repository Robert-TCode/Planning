package com.example.android.planning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "accounts.db";

    //Database Accounts Table
    private static final String TABLE_NAME = "Accounts";
    private static final String  COLUMN_ID = "id";
    private static final String  COLUMN_USERNAME = "username";
    private static final String  COLUMN_PASSWORD = "password";
    private static final String  COLUMN_PHONE_NUMBER = "phone";

    private static final String CREATE_TABLE_ACCOUNTS = "create table " + TABLE_NAME +
            " (" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_USERNAME + " text not null, " +
            COLUMN_PASSWORD + " text not null, " +
            COLUMN_PHONE_NUMBER + " integer);";

    //Database Task Table
    private static final String TABLE_TASK = "Tasks";
    private static final String  COLUMN_ID_TASK = "id_task";
    private static final String  COLUMN_USER = "name_user";
    private static final String  COLUMN_TASK = "task";

    private static final String CREATE_TABLE_TASK = " create table if not exists " + TABLE_TASK +
            " (" + COLUMN_ID_TASK + " integer primary key autoincrement, " +
            COLUMN_USER + " text not null, " +
            COLUMN_TASK + " text not null);";

    SQLiteDatabase database;
    Context context;

    /**
     * Constructor for DatabaseHelper
     */
    public DatabaseHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_TASK);
        this.database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        String query2 = "DROP TABLE IF EXISTS " + TABLE_TASK;
        db.execSQL(query2);
        this.onCreate(db);
    }

    /**
     * Insert a new account in the Accounts table if data is valid
     * Return true if success and false if fail
     */
    public boolean insertAccount(Account account) {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 'username' = '" + account.getUsername() + "';";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //There's already an account with this username
            database.close();
            return false;
        } else {
            database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_USERNAME, account.getUsername());
            values.put(COLUMN_PASSWORD, account.getPassword());
            values.put(COLUMN_PHONE_NUMBER, account.getPhoneNumber());

            database.insert(TABLE_NAME, null, values);
            cursor.close();
            database.close();
            return true;
        }
    }

    /**
     * Insert a new task in Tasks table
     */
    public void insertTask(String user, String task) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER, user);
        values.put(COLUMN_TASK, task);

        database.insert(TABLE_TASK, null, values);
        database.close();
    }

    /**
     * Check the password from Login with the Accounts data
     * Return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String username, String password) {
        database = this.getReadableDatabase();
        String query = "SELECT password FROM " + TABLE_NAME + " WHERE username = '" + username + "';";
        Cursor cursor = database.rawQuery(query, null);

        String result = null;
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }

        cursor.close();
        database.close();

        if (result != null) {
            if (result.equals(password))
                return true;
        }
        return false;
    }

    /**
     * Get all the tasks from Tasks table for an username gave as parameter
     */
    public ArrayList<String> getTasks(String username) {
        database = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_TASK + " FROM " + TABLE_TASK + " WHERE " + COLUMN_USER +
                " = '" + username + "';";
        Cursor cursor = database.rawQuery(query, null);

        ArrayList<String> results = new ArrayList<>();
        if(cursor.moveToFirst()) {
            results.add(cursor.getString(0));
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
        }

        cursor.close();
        database.close();
        if (results.isEmpty())
            return null;
        else
            return results;
    }

    /**
     * Delete a task from Tasks table with tha gaven id and tha gaven username
     */
    private void deleteTaskWithId(int index, String user) {
        database = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TASK + " WHERE " + COLUMN_ID_TASK + " = " +
                index + " AND " + COLUMN_USER + " = '" + user + "' ;";
        database.execSQL(query);
        database.close();
    }

    /**
     * Delete a task from Tasks table for tha gaven username and the gaven position in RecyclerView
     */
    public void deleteTask (int position, String username) {
        database = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID_TASK + " FROM " + TABLE_TASK + " WHERE " + COLUMN_USER +
                " = '" + username + "';";
        Cursor cursor = database.rawQuery(query, null);

        int id = -1;

        if(cursor.moveToFirst())
            if (position == 1)
                id = cursor.getInt(0);
            else {
                int pos = 2;
                while (cursor.moveToNext()) {
                    if (position == pos) {
                        id = cursor.getInt(0);
                        break;
                    }
                    pos++;
                }
            }

        cursor.close();
        database.close();
        if ( id != -1)
            deleteTaskWithId(id, username);
    }
}

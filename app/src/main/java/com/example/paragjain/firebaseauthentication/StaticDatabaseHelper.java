package com.example.paragjain.firebaseauthentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Prabhu on 11/7/2017.
 */


public class StaticDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = StaticDatabaseHelper.class.getSimpleName();
    public static final String DB_NAME = "static.db";
    public static final int DB_VERSION = 1;

    public static final String CREATE_TABLE_STATEMENT = "CREATE TABLE static(key TEXT PRIMARY KEY, value TEXT NOT NULL)";

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_STATEMENT);
    }

    public StaticDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Task.TaskEntry.TABLE);
        onCreate(db);
    }

    public String getEmail() {
        String email = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM static WHERE key = ?", new String[] {"email"});
        if (cursor != null && cursor.moveToFirst()) {
            email = cursor.getString(1);
        }
        return email;
    }

    public boolean addEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("key", "email");
        values.put("value", email);
        long err = db.insert("static", null, values);
        if (err == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int err = db.delete("static", "key = \"email\"", null);
        if (err > 0) {
            return true;
        } else {
            return false;
        }
    }

}
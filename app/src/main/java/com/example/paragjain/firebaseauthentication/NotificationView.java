package com.example.paragjain.firebaseauthentication;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paragjain.firebaseauthentication.Task;
import com.example.paragjain.firebaseauthentication.TaskHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationView extends NavBar {

    private StaticDatabaseHelper db;
    private ListView notifListView;
    private ArrayAdapter notifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_of_items_view);
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification_view, null, false);
        drawer.addView(contentView, 0);


        db = new StaticDatabaseHelper(this);
        //mHelper = new TaskHelper(this);
        notifListView = (ListView) findViewById(R.id.list_notif);



        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = ListController.getNotifications(db.getEmail());
        /*SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }
        */

        if (taskList != null) {
            if (notifAdapter == null) {
                notifAdapter = new ArrayAdapter<String>(this, R.layout.item_notif, R.id.notif_item, taskList);
                notifListView.setAdapter(notifAdapter);

            } else {
                notifAdapter.clear();
                notifAdapter.addAll(taskList);
                notifAdapter.notifyDataSetChanged();
            }
        } else if (notifAdapter != null) {
            notifAdapter.clear();
        }
        /*
        cursor.close();
        db.close();*/
    }

}


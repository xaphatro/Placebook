package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.paragjain.firebaseauthentication.ListController;
import com.google.android.gms.location.Geofence;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.example.paragjain.firebaseauthentication.R.id.action_notification;

/**
 * Created by paragjain on 11/10/17.
 */

public class ListOfListsView extends NavBar {

    private GridView listOfListsGridView;
    private ListAdapter listOfListsAdapter;
    private EditText listEditText;
    private StaticDatabaseHelper db;
    private Menu menu;

    protected void onCreate(Bundle savedInstance){
        //super.onCreate(savedInstance);
        //setContentView(R.layout.activity_list_of_lists_view);

        super.onCreate(savedInstance);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list_of_lists_view, null, false);
        drawer.addView(contentView, 0);

        db = new StaticDatabaseHelper(this);
        listOfListsGridView = (GridView) findViewById(R.id.grid_list);

        if (getIntent().getStringExtra("prevActivity") != null && getIntent().getStringExtra("prevActivity").equals("login")) {
            ArrayList<List> listHolder = ListController.getAllLists(db.getEmail());
            for (List list: listHolder){
                for (Item item: list.items){
                    if (!item.locationName.equals("null")) {
                        int x=1;
                        Geofence geofence = GeofenceController.createGeofence(Double.valueOf(item.latitude), Double.valueOf(item.longitude), item.itemID);
                        if (GeofenceActivity.getInstance() == null){
                            Intent it = new Intent(this, GeofenceActivity.class);
                            it.putExtra("end", true);
                            startActivity(it);
                        }
                        Context c = GeofenceActivity.getInstance();
                        GeofenceActivity.getInstance().addFence(geofence);
                    }
                }
            }
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        }, 0, 2000);

        //updateUI();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("", "Refreshed token: " + refreshedToken);
    }

    public void logOut(View v) {
        db.deleteEmail();
        Intent intent = new Intent(this, LoginView.class);
        startActivity(intent);
        if (GeofenceActivity.getInstance() == null){
            Intent it = new Intent(this, GeofenceActivity.class);
            it.putExtra("end", true);
            startActivity(it);
        }
        GeofenceActivity.getInstance().removeGeofences();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db.getEmail() == null) {
            Intent it = new Intent(this, LoginView.class);
            startActivity(it);
            finish();
        };
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        String notif = db.getNotification();
        if (notif != null && notif.equals("true")) {
            getMenuInflater().inflate(R.menu.notification_on, menu);
        } else {
            getMenuInflater().inflate(R.menu.notification_off, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                createDialog();
                return true;
            case R.id.action_notification:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createDialog() {
        listEditText = new EditText(this);
        listEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New List")
                .setMessage("Add a new list")
                .setView(listEditText)
                .setPositiveButton("Add List", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogue, int which) {
                        String listName = String.valueOf(listEditText.getText());
                        /*SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();*/
                        List newList = ListController.createList(db.getEmail(), listName);
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void updateUI() {
        ArrayList<List> listHolder = ListController.getAllLists(db.getEmail());
        /*SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }
        */
        if (listHolder != null) {
            if (listOfListsAdapter == null) {
                listOfListsAdapter = new ListAdapter(this, listHolder);
                listOfListsGridView.setAdapter(listOfListsAdapter);

            } else {
                listOfListsAdapter.clear();
                listOfListsAdapter.addAll(listHolder);
                listOfListsAdapter.notifyDataSetChanged();
            }
        } else if(listOfListsAdapter != null){
            listOfListsAdapter.clear();
        } else {
            int x=1;
            //error;
        }
        //cursor.close();
        //db.close();
    }

    public void goToItem(View view) {
        //View parent =(View) view.getParent();
        TextView listIDView = (TextView) view.findViewById(R.id.list_id);
        TextView listNameView = (TextView) view.findViewById(R.id.list_title);
        String listID = String.valueOf(listIDView.getText());
        String listName = String.valueOf(listNameView.getText());
        Intent it = new Intent(this, ListOfItemsView.class);
        it.putExtra("listID", listID);
        it.putExtra("listName", listName);
        startActivity(it);
    }

    public void deleteList(View view) {
        View parent = (View) view.getParent();
        //TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        TextView listIDView = (TextView) parent.findViewById(R.id.list_id);
        String listID = String.valueOf(listIDView.getText());
        /*SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE, Task.TaskEntry.COL_TASK_TITLE + " = ?", new String[] {task});
        db.close();*/
        Log.w("dellist id: ", listID);
        ListController.deleteList(listID, db);
        updateUI();
    }

    public void changePermission(View view) {
        CheckBox checkBox = (CheckBox) view;
        View parent = (View) view.getParent();
        TextView listIDView = (TextView) parent.findViewById(R.id.list_id);
        String listID = String.valueOf(listIDView.getText());
        if (checkBox.isChecked()) {
            //ListController.makeListPrivate(listID);
            ListController.makeListPublic(listID);
        } else {
            ListController.makeListPrivate(listID);
            //ListController.makeListPublic(listID);
        }
    }

    public void toggleBell(View view) {
        String notif = db.getNotification();
        if (notif != null && notif.equals("true")) {
            db.setNotificationFalse();
        } else {
            db.setNotificationTrue();
        }
    }
}

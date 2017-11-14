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

public class ListOfItemsView extends NavBar {

    private String geoJSON;
    private Geofence geofence;
    private StaticDatabaseHelper db;
    private String listID;
    private boolean geofenceAdded = false;
    //private TaskHelper mHelper;
    private ListView itemListView;
    private ItemAdapter itemAdapter;
    private String itemName;
    private EditText itemEditText;
    private Double longitude;
    private Double latitude;
    private String placeName;
    private GeofenceWrapper geofenceWrapper;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_of_items_view);
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list_of_items_view, null, false);
        drawer.addView(contentView, 0);

        context = this;
        db = new StaticDatabaseHelper(this);
        //mHelper = new TaskHelper(this);
        itemListView = (ListView) findViewById(R.id.list_todo);
        listID = getIntent().getStringExtra("listID");
        Log.w("ListID:", listID);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (itemEditText != null) {
            itemName = itemEditText.getText().toString();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listID = getIntent().getStringExtra("listID");
        updateUI();
        if (itemName != null) {
            createDialog();
            itemEditText.setText(itemName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                createDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int x=1;
                try {
                    placeName = data.getStringExtra("placeName");
                    latitude = Double.parseDouble(data.getStringExtra("latitude"));
                    longitude = Double.parseDouble(data.getStringExtra("longitude"));
                } catch (Exception e){
                    System.out.println("Exception actresult");
                    Log.w("Exception actresult", "");
                    e.printStackTrace();
                }
                x=1;
                /*
                Gson gson = new Gson();
                geoJSON = data.getStringExtra("geofence");
                try {
                    geofence = gson.fromJson(geoJSON, Geofence.class);
                } catch (Exception e){
                    e.printStackTrace();
                }
                x=1;
                */
                geofenceAdded = true;

            }
        }
    }

    private void createDialog() {
        Log.w("before get place -2", "");
        itemEditText = new EditText(this);
        itemEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("Add a new task")
                .setView(itemEditText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogue, int which) {
                        try {
                            String itemName = String.valueOf(itemEditText.getText());

                            Log.w("adding geofence", "");
                            if (geofenceAdded) {
                                //String x = item
                                String itemID = ListController.addListItem(db.getEmail(), listID, itemName, placeName, latitude.toString(), longitude.toString());
                                geofence = GeofenceController.createGeofence(latitude, longitude, placeName, itemID);
                                //geofence.setRequestId(itemID);
                                GeofenceActivity.getInstance().addFence(geofence);
                            }
                            else{
                                Log.w("gefoence not found", "");
                                ListController.addListItem(db.getEmail(), listID, itemName);
                            }
                            updateUI();
                        }catch (Exception e){
                            Log.w("exception", "e");
                            e.printStackTrace();
                        }
                    }
                })
                .setNeutralButton("Add Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w("before get place -1", "");
                        Intent intent = new Intent(getBaseContext(), GeofenceActivity.class);
                        startActivityForResult(intent, 1);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /*
    private void createGeoFence() {
        Geofence geo = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId((String) placeName)
                // Set the circular region of this geofence.
                .setCircularRegion(
                        latitude,
                        longitude,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )
                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                // Create the geofence.
                .build();
        /*if (!checkPermissions()) {
            mPendingGeofenceTask = PendingGeofenceTask.ADD;
            requestPermissions();
            return;
        }

        addGeoFence(geo);
    }
    */

    /*
    private void addGeofence(Geofence geo){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(geo), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }
    */

    private void updateUI() {
        ArrayList<Item> taskList = ListController.getListItems(db.getEmail(), listID);
        /*SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }
        */
        if (taskList != null) {
            if (itemAdapter == null) {
                itemAdapter = new ItemAdapter(this, taskList);
                itemListView.setAdapter(itemAdapter);

            } else {
                itemAdapter.clear();
                itemAdapter.addAll(taskList);
                itemAdapter.notifyDataSetChanged();
            }
        } else if (itemAdapter != null) {
            itemAdapter.clear();
        }
        /*
        cursor.close();
        db.close();*/
    }

    public void deleteItem(View view) {
        View parent = (View) view.getParent();
        //TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        TextView itemIDView = (TextView) parent.findViewById(R.id.item_id);
        String itemID = String.valueOf(itemIDView.getText());
        /*SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE, Task.TaskEntry.COL_TASK_TITLE + " = ?", new String[] {task});
        db.close();*/
        ListController.deleteItem(listID, itemID);
        updateUI();
    }
}


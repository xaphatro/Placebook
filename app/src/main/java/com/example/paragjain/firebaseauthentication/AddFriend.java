package com.example.paragjain.firebaseauthentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rahul on 14/11/17.
 */

public class AddFriend extends NavBar {
    StaticDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new StaticDatabaseHelper(this);
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_add_friend_view, null, false);
        drawer.addView(contentView, 0);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        }, 0, 2000);


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
            case R.id.action_notification:
                db.setNotificationFalse();
                Intent it = new Intent(this, NotificationView.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addFriend(View v)
    {
        String email = db.getEmail();
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("src_email", email);
        EditText fEmail = (EditText) findViewById(R.id.etFriendEmail);
        String friendEmail=fEmail.getText().toString().trim();
        arguments.put("dest_email", friendEmail);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/addfriend");

        queryapi q = new queryapi(arguments);
        try
        {
            String res= q.execute().get();
            Log.w("addfriend check: ","val:"+res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("addFriend status code: ","val:"+ status);
            if(status==200)//if(db.getUser(getEmail, getPassword))
            {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch(JSONException e)
        {
            Log.w("catch block: ","");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            Log.w("catch exception block: ","");
            e.printStackTrace();
        }
    }
}

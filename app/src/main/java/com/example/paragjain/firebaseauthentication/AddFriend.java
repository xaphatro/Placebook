package com.example.paragjain.firebaseauthentication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

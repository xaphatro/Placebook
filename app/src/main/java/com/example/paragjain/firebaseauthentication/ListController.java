package com.example.paragjain.firebaseauthentication;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rahul on 11/11/17.
 */

public class ListController {
    public void createList(String listName, String email, String secret){
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("listName", listName);
        arguments.put("email", email);
        arguments.put("secret", secret);
        arguments.put("url", "http://locationreminder.azurewebsites.net/createlist");

        queryapi q = new queryapi(arguments);
        try
        {
            String res= q.execute().get();
            Log.w("check: ","val:"+res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("status code result : ","val:"+ status);
            if(status==200)//if(db.getUser(getEmail, getPassword))
            {
                int listID = resultJSON.getInt("list_id");
                List li = new List(listID, listName);
            }
            else
            {

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

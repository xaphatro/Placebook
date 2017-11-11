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
    public static List createList(String email, String listName){
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("listName", listName);
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
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
                li = new List(listID, listName);
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
        return li;
    }

    public static List getAllLists(String email){
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/getalllists");

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
                //li = new List(listID, listName);
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
        return null;
    }

    public static void addListItem(String email, int listID, String itemName, double latitude, double longitude){
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("itemName", itemName);
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("listID", String.valueOf(listID));
        arguments.put("latitude", String.valueOf(latitude));
        arguments.put("longitude", String.valueOf(longitude));
        arguments.put("url", "http://locationreminder.azurewebsites.net/addItem");

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
                int itemID = resultJSON.getInt("itemID");
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

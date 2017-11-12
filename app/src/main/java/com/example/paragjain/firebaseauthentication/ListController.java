package com.example.paragjain.firebaseauthentication;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by rahul on 11/11/17.
 */

public class ListController {
    public static List createList(String email, String listName){
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("list_name", listName);
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/createlist");

        queryapi q = new queryapi(arguments);
        try
        {
            String res= q.execute().get();
            Log.w("create lists check: ","val:"+res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("crelists status code: ","val:"+ status);
            if(status==200)//if(db.getUser(getEmail, getPassword))
            {
                String listID = resultJSON.getString("list_id");
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

    public static ArrayList<List> getAllLists(String email){
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/getalllists");

        ArrayList<List> listArray = null;

        queryapi q = new queryapi(arguments);
        try
        {
            String res= q.execute().get();
            Log.w("alllists check: ","val:"+res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("alllists status code : ","val:"+ status);
            if(status==200)//if(db.getUser(getEmail, getPassword))
            {
                listArray = new ArrayList<List>();
                JSONArray lists = resultJSON.getJSONArray("lists");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject currList = lists.getJSONObject(i);
                    String listID = currList.getString("list_id");
                    String listName = currList.getString(("title"));
                    List li = new List(listID, listName);
                    listArray.add(li);
                }
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
        return listArray;
    }

    public static void deleteList(String email, String listID){
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("list_id", listID);
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/deletelist");

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
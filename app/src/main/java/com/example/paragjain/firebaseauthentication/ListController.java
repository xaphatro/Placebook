package com.example.paragjain.firebaseauthentication;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by rahul on 11/11/17.
 */

public class ListController {
    public static List createList(String email, String listName) {
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("list_name", listName);
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/createlist");

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("create lists check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("crelists status code: ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                String listID = resultJSON.getString("list_id");
                li = new List(listID, listName);
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
        return li;
    }

    public static ArrayList<List> getAllLists(String email) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/getalllists");

        ArrayList<List> listArray = null;

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("alllists check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("alllists status code : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                listArray = new ArrayList<List>();
                JSONArray lists = resultJSON.getJSONArray("lists");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject currList = lists.getJSONObject(i);
                    String listID = currList.getString("list_id");
                    String listName = currList.getString("title");
                    JSONArray items = currList.getJSONArray("items");
                    ArrayList<Item> itemList = new ArrayList<>();

                    for (int j = 0; j < items.length(); j++){
                        JSONObject currItem = items.getJSONObject(j);
                        String itemID = currItem.getString("item_id");
                        String itemEmail = currItem.getString("email");
                        String itemName = currItem.getString("item_name");
                        String locationName = currItem.getString("location_name");
                        String longitude = currItem.getString("longitude");
                        String latitude = currItem.getString("done");
                        Item item = new Item(itemID, itemName, locationName, longitude, latitude);
                        itemList.add(item);
                    }
                    List li = new List(listID, listName, itemList);
                    switch (items.length()) {
                        case 0:
                            break;
                        case 1:
                            li.taskOne = itemsList.get(0).itemName;
                            break;
                        case 2:
                            li.taskOne = itemsList.get(0).itemName;
                            li.taskTwo = itemsList.get(1).itemName;
                            break;
                        default:
                            li.taskOne = itemsList.get(0).itemName;
                            li.taskTwo = itemsList.get(1).itemName;
                            li.taskThree = itemsList.get(2).itemName;
                            break;
                    }
                    listArray.add(li);
                }
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
        return listArray;
    }

    public static void deleteList(String listID, StaticDatabaseHelper db) {
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("list_id", listID);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/deletelist");

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("status code result : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                ArrayList<Item> itemList = getListItems(db.getEmail(), listID);
                for (Item item : itemList) {
                    deleteItem(listID, item.itemID);
                }
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
    }

    public static ArrayList<Item> getListItems(String email, String listID) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);
        arguments.put("list_id", listID);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/getlistcontents");

        ArrayList<Item> itemArray = null;

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("itelists check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("itelists status code : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                itemArray = new ArrayList<Item>();
                JSONArray items = resultJSON.getJSONArray("rows");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject currList = items.getJSONObject(i);
                    String itemID = currList.getString("item_id");
                    String itemName = currList.getString("item_name");
                    String locationName = currList.getString("location_name");
                    String longitude = currList.getString("longitude");
                    String latitude = currList.getString("latitude");
                    Item it = new Item(itemID, itemName, locationName, longitude, latitude);
                    itemArray.add(it);
                }
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
        return itemArray;
    }

    public static String addListItem(String email, String listID, String itemName, String location, String latitude, String longitude) {
        Item it = null;
        String itemID = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("item_name", itemName);
        arguments.put("email", email);
        arguments.put("list_id", listID);
        arguments.put("location_name", location);
        arguments.put("latitude", latitude);
        arguments.put("longitude", longitude);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/addItem");

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("status code result : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                itemID = resultJSON.getString("item_id");
                it = new Item(itemID, itemName, location, longitude, latitude);
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }

        return itemID;
    }

    public static Item addListItem(String email, String listID, String itemName) {
        Item it = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("item_name", itemName);
        arguments.put("email", email);
        arguments.put("list_id", listID);
        arguments.put("location_name", "null");
        arguments.put("latitude", "null");
        arguments.put("longitude", "null");
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/addItem");

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("status code result : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                String itemID = resultJSON.getString("itemID");
                it = new Item(itemID, itemName);
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
        return it;

    }

    public static void deleteItem(String listID, String itemID) {
        List li = null;
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("list_id", listID);
        arguments.put("item_id", itemID);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/deleteitem");

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("status code result : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                GeofenceActivity.getInstance().removeGeofences(itemID);
            } else {
                Log.w("status code result : ", "val:" + status);
            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
    }

    public static ArrayList<Friend> getFriends(String email) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/viewfriends");

        ArrayList<Friend> friendArray = null;

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            //Log.w("alllists check: ","val:"+res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("Friends status code : ", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                friendArray = new ArrayList<Friend>();
                JSONArray lists = resultJSON.getJSONArray("friends");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject currList = lists.getJSONObject(i);
                    String friendName = currList.getString("name");
                    String friendEmail = currList.getString("email");
                    Friend f = new Friend(friendName, friendEmail);
                    friendArray.add(f);
                }
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
        return friendArray;
    }

    public static HashMap<String, String> getNotificationDetails(String itemID){
        HashMap<String, String> arguments = new HashMap<>();
        HashMap<String, String> notificationDetails = null;
        arguments.put("item_id", itemID);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/getnotificationdetails");
        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("notfication", itemID);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            if(status == 200){
                notificationDetails = new HashMap<>();
                notificationDetails.put("location_name", resultJSON.getString("location_name"));
                notificationDetails.put("item_name", resultJSON.getString("item_name"));
            } else {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notificationDetails;
    }
    public static ArrayList<List> getPeerLists(String email) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/viewpeerlists");

        ArrayList<List> listArray = null;

        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("peerlists check: ", "val:" + res);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            Log.w("peerlists status code :", "val:" + status);
            if (status == 200)//if(db.getUser(getEmail, getPassword))
            {
                listArray = new ArrayList<List>();
                JSONArray lists = resultJSON.getJSONArray("lists");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject currList = lists.getJSONObject(i);
                    String listID = currList.getString("list_id");
                    String listName = currList.getString("title");
                    List li = new List(listID, listName);
                    listArray.add(li);
                }
            } else {

            }
        } catch (JSONException e) {
            Log.w("catch block: ", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("catch exception block: ", "");
            e.printStackTrace();
        }
        return listArray;
    }

    public static void sendToken(String refreshedToken) {
        HashMap<String, String> arguments = new HashMap<>();
        HashMap<String, String> notificationDetails = null;
        arguments.put("new_token", refreshedToken);
        StaticDatabaseHelper db = new StaticDatabaseHelper(LoginView.getInstance());//
        arguments.put("email", db.getEmail());
        arguments.put("secret", Constants.SERVER_SECRET_KEY);
        arguments.put("url", "http://locationreminder.azurewebsites.net/tokenregistration");
        queryapi q = new queryapi(arguments);
        try {
            String res = q.execute().get();
            Log.w("notfication", refreshedToken);

            JSONObject resultJSON = new JSONObject(res);
            int status = resultJSON.getInt("status");
            if(status == 200){
                Log.w("success", "");
            } else {
                Log.w("failure", "");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.example.paragjain.firebaseauthentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FcmForegroundService extends FirebaseMessagingService {

    StaticDatabaseHelper db;

    public FcmForegroundService() {
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("", "Message data payload: " + remoteMessage.getData());
            JSONObject resultJSON = new JSONObject(remoteMessage.getData());

            ArrayList itemArray = new ArrayList<Item>();
            try {
                JSONObject item = resultJSON;
                String itemID = item.getString("item_id");
                String itemName = item.getString("item_name");
                String locationName = item.getString("location_name");
                String longitude = item.getString("longitude");
                String latitude = item.getString("latitude");
                //Item it = new Item(itemID, itemName, locationName, longitude, latitude);

                Intent it = new Intent(this, GeofenceNotificationAdder.class);
                it.putExtra("latitude", latitude);
                it.putExtra("longitude", longitude);
                it.putExtra("itemID", itemID);
                //startActivity(it);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        db = new StaticDatabaseHelper(Placebook.getInstance());
        db.setNotificationTrue();

        // Also if you intend on generating your own notifications as a result of a received FCM

        // message, here is where that should be initiated. See sendNotification method below.
    }
   /* @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
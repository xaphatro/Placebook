package com.example.paragjain.firebaseauthentication;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by rahul on 15/11/17.
 */

public class FcmBackgroundManager extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.w("", "Refreshed token: " + refreshedToken);
            Log.d("Refreshed token: ", refreshedToken);
            //System.out.println("Refreshed token: " + refreshedToken);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Log.d("What up", "FcmBackgroundManager");
        super.onTaskRemoved(rootIntent);
        this.stopSelf();
    }


}

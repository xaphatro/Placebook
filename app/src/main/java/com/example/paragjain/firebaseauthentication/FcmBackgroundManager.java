package com.example.paragjain.firebaseauthentication;

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
            //System.out.println("Refreshed token: " + refreshedToken);
            ListController.sendToken(refreshedToken);
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
}

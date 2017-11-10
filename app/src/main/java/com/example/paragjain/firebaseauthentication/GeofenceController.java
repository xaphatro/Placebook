package com.example.paragjain.firebaseauthentication;

import android.*;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

/**
 * Created by rahul on 10/11/17.
 */

public class GeofenceController {

    public static PendingIntent mGeofencePendingIntent;
    public static GeofencingClient mGeofencingClient;

    private static GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);

        return builder.build();
    }

    public static PendingIntent getGeofencePendingIntent(Context c) {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(c, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void addGeofence(Geofence geo, Context c){
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mGeofencingClient.addGeofences(getGeofencingRequest(geo), getGeofencePendingIntent(c)).addOnCompleteListener(c);
    }

    /*
    PendingIntent getmGeofencePendingIntent(){
        return mGeofencePendingIntent;
    }

    GeofencingClient initGeofencingClient(){
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        return mGeofencingClient;
    }*/
}

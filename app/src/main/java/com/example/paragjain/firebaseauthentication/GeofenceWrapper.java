package com.example.paragjain.firebaseauthentication;

import android.util.Log;

import com.google.android.gms.location.Geofence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Nikhil Prabhu on 11/11/2017.
 */

public class GeofenceWrapper implements Serializable{
    public Geofence geofence;

    public GeofenceWrapper(Geofence g){
        this.geofence = g;
    }
}

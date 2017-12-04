package com.example.paragjain.firebaseauthentication;

import android.app.Application;
import android.content.Intent;

/**
 * Created by rahul on 15/11/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, KeepRunning.class));
    }
}

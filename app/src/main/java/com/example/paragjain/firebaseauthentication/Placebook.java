package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Nikhil Prabhu on 11/17/2017.
 */

public class Placebook extends Activity {
    static Context placebookContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placebookContext = this;
        Intent it = new Intent(this, LoginView.class);
        startActivity(it);
        finish();
    }

    public static Context getInstance(){
        return placebookContext;
    }

}

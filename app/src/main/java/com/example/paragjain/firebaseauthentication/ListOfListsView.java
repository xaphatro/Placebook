package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * Created by paragjain on 11/10/17.
 */

public class ListOfListsView extends Activity {

    StaticDatabaseHelper db;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_list_of_lists_view);
        db = new StaticDatabaseHelper(this);
    }

    public void goToItems(View v) {
        Intent intent = new Intent(this, ListOfItemsView.class);
        startActivity(intent);
    }

    public void logOut(View v) {
        db.deleteEmail();
        Intent intent = new Intent(this, LoginView.class);
        startActivity(intent);
        finish();
    }
}

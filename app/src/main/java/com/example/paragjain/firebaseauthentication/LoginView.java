package com.example.paragjain.firebaseauthentication;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginView extends AppCompatActivity {

    static Context loginContext;

    public static final String TAG = LoginView.class.getSimpleName();
    private EditText email, password, name;
    private Button login;
    private TextView signup;
    private StaticDatabaseHelper db;
    private Session session;
    final int interval = 7000; //7 second
    private Handler handler = new Handler();
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        loginContext = this;

        Log.d(TAG, "login activity.");
        session = new Session(this);
        db = new StaticDatabaseHelper(this);
        //db = new DbHelper();
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        name = (EditText) findViewById(R.id.etName);
        login = (Button) findViewById(R.id.bLogin);
        signup = (TextView) findViewById(R.id.bSignUp);

        if (!checkPermissions()) {
            requestPermissions();
        }

        /*
        if(session.loggedin()){
            Intent intent = new Intent(this, ListOfListsView.class);
            startActivity(intent);
            finish();
        }
        */
        if (db.getEmail() != null){
            Intent intent = new Intent(this, ListOfListsView.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void requestPermissions() {
        final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(LoginView.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(LoginView.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public static Context getInstance(){
        return loginContext;
    }

    public void goToLists(View v) {
        if (db.getEmail() == null) {
            db.addEmail("nishantb21@gmail.com");
        }
        else {
            db.deleteEmail();
            db.addEmail("nishantb21@gmail.com");
        }
        Intent intent = new Intent(this, ListOfListsView.class);
        startActivity(intent);
        finish();
    }

    public void signUp(View v){
        Log.d(TAG, "sign Up button clicked.");
        Intent intent = new Intent(LoginView.this, SignUpView.class);
        finish();
        startActivity(intent);
    }

    public void login(View v){

        Log.d("login","called");
        String emailContent = email.getText().toString().trim();
        String passwordContent = password.getText().toString().trim();
        //String secret = "fb943a2432995dc8114f15f868bbec305fac35b82e610286a2155e807cb577d4";

        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", emailContent);
        arguments.put("password", passwordContent);
        arguments.put("url", "http://locationreminder.azurewebsites.net/login");
        arguments.put("secret", Constants.SERVER_SECRET_KEY);

        myTimer();
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
                //session.setLoggedIn(true);
                handler.removeCallbacks(runnable);
                Intent it = new Intent(LoginView.this, ListOfListsView.class);
                if (db.getEmail() == null) {
                    db.addEmail(emailContent);
                }
                else {
                    db.deleteEmail();
                    db.addEmail(emailContent);
                }
                if(db.getTokenSet().equals("true")) {
                    Log.d("here","login");
                    ListController.sendToken(db.getToken());
                    //db.setTokenSetFalse();
                }
                it.putExtra("prevActivity", "login");
                startActivity(it);

                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Wrong email/password", Toast.LENGTH_SHORT).show();
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

    private void myTimer()
    {

        runnable = new Runnable(){
            public void run(){
                Toast.makeText(LoginView.this, "App could not connect to the server.Retry.", Toast.LENGTH_SHORT).show();
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

}
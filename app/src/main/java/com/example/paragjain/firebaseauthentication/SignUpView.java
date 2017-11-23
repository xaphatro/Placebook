package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.RunnableFuture;
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 * Created by paragjain on 11/10/17.
 */

public class SignUpView extends Activity {


    private EditText name;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private TextView signUp;
    private StaticDatabaseHelper db;
    private Session session;
    final int interval = 7000; //7 second
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_view);
        db = new StaticDatabaseHelper(this);

        name = (EditText) findViewById(R.id.etName);
        email = (EditText) findViewById(R.id.etEmail);
        phoneNumber = (EditText) findViewById(R.id.etPhone);
        password = (EditText) findViewById(R.id.etPassword);
        signUp = (TextView) findViewById(R.id.bSignUp);
    }

    public void logIn(View v) {
        Intent intent = new Intent(SignUpView.this, LoginView.class);
        finish();
        startActivity(intent);
    }

    public void signUp(View v){


        myTimer();

        final String nameContent = name.getText().toString().trim();
        final String emailContent = email.getText().toString().trim();
        final String phoneNumberContent = phoneNumber.getText().toString().trim();
        final String passwordContent = password.getText().toString().trim();
        //final String secret = "fb943a2432995dc8114f15f868bbec305fac35b82e610286a2155e807cb577d4";

        //UPDATE THE DATABASE ( CLOUD ) HERE THROUGH API CALL. ADD HIM TO THE USERS TABLE
        if(emailContent.isEmpty() && nameContent.isEmpty() && phoneNumberContent.isEmpty() && passwordContent.isEmpty()){
            Toast.makeText(SignUpView.this, "All fields must be filled. Field empty", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("name", nameContent);
            arguments.put("email", emailContent);
            arguments.put("phoneno", phoneNumberContent);
            arguments.put("password", passwordContent);
            arguments.put("secret", Constants.SERVER_SECRET_KEY);
            arguments.put("url", "http://locationreminder.azurewebsites.net/signup");
            queryapi q = new queryapi(arguments);
            //q.execute();

            try
            {
                String res= q.execute().get();
                Log.d("check: ","val:"+res);

                JSONObject resultJSON = new JSONObject(res);
                int status = resultJSON.getInt("status");
                Log.w("status code result : ","val:"+ status);
                Log.d("status code result : ","val:"+ status);

                if(status==200)//if(db.getUser(getEmail, getPassword))
                {
                    //session.setLoggedIn(true);
                    //handler.removeCallbacks(runnable);
                    Log.d("signUp:status", "200 true");
                    Intent it = new Intent(SignUpView.this, ListOfListsView.class);
                    //UserInfo.USER_EMAIL = emailContent;
                    if (db.getEmail() == null) {
                        db.addEmail(emailContent);
                    }
                    else {
                        db.deleteEmail();
                        db.addEmail(emailContent);
                    }
                    boolean val = db.getTokenSet().equals("true");
                    if(db.getTokenSet().equals("true")) {
                        Log.d("here","login");
                        ListController.sendToken(db.getToken());
                        //db.setTokenSetFalse();
                    }
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

        SharedPreferences preferences = getSharedPreferences("MeraPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(emailContent+passwordContent+"data", emailContent+"\n"+passwordContent);
        editor.commit();

    }


    private void myTimer()
    {

        Log.d("myTimer", "signUp");
        runnable = new Runnable(){
            public void run(){
                Log.d("run", "signUp");
                Toast.makeText(SignUpView.this, "App could not connect to the server.Retry.", Toast.LENGTH_SHORT).show();
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

}
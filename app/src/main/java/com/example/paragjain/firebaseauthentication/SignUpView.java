package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by paragjain on 11/10/17.
 */

public class SignUpView extends Activity {


    private EditText name;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private Button signUp;
    private StaticDatabaseHelper db;
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_view);
        db = new StaticDatabaseHelper(this);
        /*
        /name = (EditText) findViewById(R.id.etName);
        email = (EditText) findViewById(R.id.etEmail);
        phoneNumber = (EditText) findViewById(R.id.etPhone);
        password = (EditText) findViewById(R.id.etPassword);
        signUp = (Button) findViewById(R.id.bSignUp);*/
    }

    public void signUp(View v){
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
            q.execute();

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
                    Intent it = new Intent(SignUpView.this, ListOfListsView.class);
                    //UserInfo.USER_EMAIL = emailContent;
                    if (db.getEmail() == null) {
                        db.addEmail(emailContent);
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

}
package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginView extends AppCompatActivity {

    static LoginView loginContext;

    public static final String TAG = LoginView.class.getSimpleName();
    private EditText email, password, name;
    private Button login, signup;
    private StaticDatabaseHelper db;
    private Session session;

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
        signup = (Button) findViewById(R.id.bSignUp);
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
    public static LoginView getInstance(){
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
        String emailContent = email.getText().toString().trim();
        String passwordContent = password.getText().toString().trim();
        //String secret = "fb943a2432995dc8114f15f868bbec305fac35b82e610286a2155e807cb577d4";

        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", emailContent);
        arguments.put("password", passwordContent);
        arguments.put("url", "http://locationreminder.azurewebsites.net/login");
        arguments.put("secret", Constants.SERVER_SECRET_KEY);

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
                Intent it = new Intent(LoginView.this, ListOfListsView.class);
                if (db.getEmail() == null) {
                    db.addEmail(emailContent);
                }
                else {
                    db.deleteEmail();
                    db.addEmail(emailContent);
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
}
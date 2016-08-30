package com.example.ypwang.myapplication;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";


    static final String APP_ID = "40836";
    static final String AUTH_KEY = "ZZ3WsWGkRVPQWma";
    static final String AUTH_SECRET = "KVrQPXAFbO53sYy";
    static final String ACCOUNT_KEY = "gMiWbSQZJTqYcGYHySpn";

    EditText editTextEmail;
    EditText editTextPassword;
    WebApiReceiver api;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                Log.v("QB","Create session OK");
            }

            @Override
            public void onError(QBResponseException error) {
                Log.v("QB","Create session error");
            }
        });

        api = new WebApiReceiver(this);


    }

    private boolean isAccountFormatValid(String email, String passwd){
        Pattern emailPattern = Pattern.compile("^([^@\\s]+)@([^@\\s]+)\\s?$");
        if(email.length() == 0){
            Log.v("Login ","Empty email address");
            return false;
        }
        else {
            Matcher m = emailPattern.matcher(email);
            if(m.find()){
                Log.v("Login:user ", m.group());
                Log.v("Login:password ",passwd);
                return true;
            }
            else{
                Toast.makeText(MainActivity.this, "Wrong format", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public void buttonLoginClicked(View view) {

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(isAccountFormatValid(email,password)) {
            final QBUser user = new QBUser("zx20a", password);
            // Login
            QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser user, Bundle args) {

                    Log.v("Login:QBlogin", "OK");
                }

                @Override
                public void onError(QBResponseException error) {
                    Log.v("Login:QBlogin", "Failed");
                }
            });
        }
    }
    public void buttonFbLoginClicked(View view) {
        Toast.makeText(MainActivity.this, "FB Login clicked", Toast.LENGTH_SHORT).show();
        api.doInBackground();
    }
    public void buttonSignClicked(View view) {
        Toast.makeText(MainActivity.this, "Signup clicked", Toast.LENGTH_SHORT).show();

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(isAccountFormatValid(email,password)) {
            // Register new user
            final QBUser user = new QBUser(email, password);

            QBUsers.signUp(user, new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser user, Bundle args) {
                    Log.v("Login:QB signup", "OK");
                }

                @Override
                public void onError(QBResponseException error) {
                    Log.v("Login:QB signup", "Failed");
                }
            });
        }
    }
    public void buttonLossPwClicked(View view) {
        Toast.makeText(MainActivity.this, "Loss password clicked", Toast.LENGTH_SHORT).show();
    }
}

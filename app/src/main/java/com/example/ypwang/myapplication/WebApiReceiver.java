package com.example.ypwang.myapplication;
/**
 * Created by ypwang on 2016/8/30.
 */
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebApiReceiver extends AsyncTask<Void, Void, String> {

    public Activity activity;
    public ProgressBar progressBar;

    public WebApiReceiver( Activity _activity){

        this.activity = _activity;
        this.progressBar = (ProgressBar)this.activity.findViewById(R.id.progressBar);
//other initializations...

    }
    private Exception exception;



    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected String doInBackground(Void... urls) {
        String email = "admin@gmail.com";
        // Do some validation here

        try {
            URL url = new URL("http://220.133.185.190:8889/login" + "email=" + email + "&apiKey=" + 123456);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
        //responseView.setText(response);
    }
}
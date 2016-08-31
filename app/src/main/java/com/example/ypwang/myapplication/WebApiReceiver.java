package com.example.ypwang.myapplication;
/**
 * Created by ypwang on 2016/8/30.
 */
import android.support.v7.app.AppCompatActivity;



import android.app.Activity;
import android.os.AsyncTask;

import android.view.View;
import android.view.ViewDebug;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.core.rest.HTTPPostTask;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.util.Log;
import android.util.Pair;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebApiReceiver extends AsyncTask<Pair<String, String>, Void, String> {

    public Activity activity;
    public ProgressBar progressBar;
    HttpsURLConnection conn;
    TrustManager[] trust;

    public WebApiReceiver( Activity _activity){

        this.activity = _activity;
        this.progressBar = (ProgressBar)this.activity.findViewById(R.id.progressBar);
        trust = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i("TrustManager", "checkClientTrusted");
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i("TrustManager", "checkServerTrusted");
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                //return new X509Certificate[0];
                return null;
            }
        }};

        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }

            });
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trust, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

//other initializations...

    }
    private Exception exception;



    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected String doInBackground(Pair<String, String>... param) {
        String apiUrl = "null";
        List<Pair<String,String>> params = new ArrayList<Pair<String,String>>();

        for (Pair<String, String> pair : param){
            Log.v("api param:", pair.first);
            Log.v("api param:", pair.second);

            if(pair.first.equals("URL")) {
                apiUrl = pair.second;
            }
            else {
                params.add(pair);
            }
        }
        if(apiUrl.equals("null"))
            return null;

        try {
            URL url = new URL(apiUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setConnectTimeout(10000);
//            conn.setRequestProperty("Content-length", "0");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            //conn.connect();

            try {
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
               // conn.connect();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                Log.v("reader", stringBuilder.toString());
                return stringBuilder.toString();
            }
            finally{
                conn.disconnect();
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

    private String getQuery(List<Pair<String,String>> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String,String> pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }
        Log.v("Query params", result.toString());
        return result.toString();
    }
}
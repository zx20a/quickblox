package com.example.ypwang.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView textView = new TextView(this);
        Intent intent = getIntent();
        Toast.makeText(Main2Activity.this, intent.getStringExtra(MainActivity.EXTRA_MESSAGE), Toast.LENGTH_SHORT).show();
        textView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        layout.addView(textView);
    }

    public void backToMain(View view) {

    }
}

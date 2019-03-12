package com.mailalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        TimerTask ts=new TimerTask() {
            @Override
            public void run() {
                Intent it=new Intent(MainActivity.this,CredentialActivity.class);
                startActivity(it);
                finish();
            }
        };
        new Timer().schedule(ts,3000);
    }


}

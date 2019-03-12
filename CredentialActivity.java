package com.mailalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CredentialActivity extends AppCompatActivity {

    Button bt1;
    String pin;
    EditText et1;
    SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);
        bt1=(Button)findViewById(R.id.bt1);
        et1=(EditText)findViewById(R.id.et1);
        spf=getSharedPreferences("setup",0);
       pin=spf.getString("key",null);
        if(pin!=null)
        {
            bt1.setText("Signin");
        }
    }

    public void doAction(View view) {

        String pin=et1.getText().toString().trim();

        if(bt1.getText().equals("Signup"))
    {
        SharedPreferences.Editor ed=spf.edit();

        if(pin.length()!=4)
            et1.setError("Enter a valid pin of 4 digits");
        else {
            ed.putString("key", pin);
            ed.commit();
            Intent it=new Intent(this,HomeActivity.class);
            startActivity(it);
            finish();

        }

    }
    else
    {
        if(pin.equals(this.pin))
        {
            Intent it=new Intent(this,HomeActivity.class);
            startActivity(it);
            finish();
        }
        else
            Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
    }
    }
}

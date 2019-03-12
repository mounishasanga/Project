package com.mailalert;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements View.OnKeyListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
Switch sp1;
SharedPreferences prefs;
EditText et_email,et_altemail,fromtime,tottime;
CheckBox chs[];

int ids[]={R.id.miss,R.id.mess,R.id.bst,R.id.mon,R.id.tue,R.id.wed,R.id.thu,R.id.fri,R.id.sat,R.id.sun};
String names[]={"Missed Calls","Messages","Battery Status","Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prefs=getSharedPreferences("prefs",0);
        sp1=(Switch)findViewById(R.id.sp1);
        et_email=(EditText)findViewById(R.id.et_email);
        et_altemail=(EditText)findViewById(R.id.et_alemail);
        fromtime=(EditText)findViewById(R.id.stime);
        tottime=(EditText)findViewById(R.id.etime);
        et_email.setOnKeyListener(this);
        et_altemail.setOnKeyListener(this);

        fromtime.setOnFocusChangeListener(this);
        tottime.setOnFocusChangeListener(this);

        sp1.setChecked(prefs.getBoolean("on",false));
        sp1.setOnCheckedChangeListener(this);
        chs=new CheckBox[10];

        for(int i=0;i<10;i++) {
            chs[i]=(CheckBox)findViewById(ids[i]);
            chs[i].setOnClickListener(this);

        }

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CONTACTS},123);

    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {


        sp1.setChecked(prefs.getBoolean("on",false));

        for(int i=0;i<10;i++) {
            chs[i].setChecked(prefs.getBoolean(names[i], false));
        }
            et_email.setText(prefs.getString("email",""));
            et_altemail.setText(prefs.getString("alemail",""));
            fromtime.setText(prefs.getString("stime",""));
            tottime.setText(prefs.getString("etime",""));



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(!(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED )) {
            Toast.makeText(this, "Must provide Permission", Toast.LENGTH_SHORT).show();
        finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void doAction(View view) {


SharedPreferences.Editor ed=prefs.edit();
        for(int i=0;i<10;i++) {
            ed.putBoolean(names[i], chs[i].isChecked());
        }
ed.putString("email",et_email.getText().toString());
ed.putString("alemail",et_altemail.getText().toString());
            ed.putString("stime",fromtime.getText().toString());
ed.putBoolean("on",sp1.isChecked());
            ed.putString("etime",tottime.getText().toString());
ed.commit();
            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();

        }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction()!=KeyEvent.ACTION_UP) {
            String rs = ((EditText) v).getText().toString();
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(v.getTag().toString(), rs);
            ed.commit();
        }
        return false;
    }

    @Override
    public void onFocusChange(final View v, boolean hasFocus) {
        if (hasFocus) {

            TimePickerDialog td = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    ((EditText) v).setText(hourOfDay + "." + minute);
                    SharedPreferences.Editor ed = prefs.edit();

                    ed.putString(v.getTag().toString(), hourOfDay + "." + minute);
                    ed.commit();

                }
            }, 10, 0, true);
            td.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor ed=prefs.edit();

        ed.putBoolean("on",isChecked);
        ed.commit();
    }

    @Override
    public void onClick(View v) {
        CheckBox ch=(CheckBox)v;
        SharedPreferences.Editor ed=prefs.edit();

        ed.putBoolean(ch.getText().toString(),ch.isChecked());
        ed.commit();
    }
}


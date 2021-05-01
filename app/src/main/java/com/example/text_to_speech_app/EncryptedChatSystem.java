package com.example.text_to_speech_app;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;

import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.appcompat.app.AppCompatActivity;



public class EncryptedChatSystem extends AppCompatActivity {

    private EditText et_phone;
    private ImageButton contacts;

    private EditText et_message;
    private EditText et_password;

    private Button bt_send;
    private Button bt_decrypt;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted_chat_system);

        et_phone = findViewById(R.id.et_phone);
        contacts = findViewById(R.id.bt_contacts);

        et_message = findViewById(R.id.et_message);
        et_password = findViewById(R.id.et_password);

        bt_send = findViewById(R.id.bt_send);
        bt_decrypt = findViewById(R.id.bt_decrypt);


        bt_send.setVisibility(View.VISIBLE);
        bt_decrypt.setVisibility(View.GONE);

        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},2);

            try {
                receiveSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                receiveSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TelephonyManager telephonyManager= (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},3);
        }


}
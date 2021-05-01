package com.example.text_to_speech_app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedChatSystem extends AppCompatActivity {

    private EditText et_phone;
    private ImageButton contacts;

    private EditText et_message;
    private EditText et_password;

    private Button bt_send;
    private Button bt_decrypt;

    String AES="AES";
    String string="";
    String outputString="";
    String tempEncrypted="";


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


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                        if(et_message.length()>0 && et_phone.length()>0 && et_phone.length()<11 && et_password.length()!=0) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    outputString = encrypt(et_message.getText().toString(), et_password.getText().toString());
                                }
                                tempEncrypted = outputString;
//                        outputText.setText(outputString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            sendSMS();

                            et_message.setText("");
                            et_phone.setText("");
                            et_password.setText("");
                            bt_send.setVisibility(View.GONE);
                            bt_decrypt.setVisibility(View.VISIBLE);
                        }
                        else if(et_phone.length()==0)
                        {
                            Toast.makeText(getApplicationContext(), "Please enter a number", Toast.LENGTH_LONG).show();
                            et_phone.requestFocus();
                        }
                        else if(et_message.length()==0)
                        {
                            Toast.makeText(getApplicationContext(), "Please write your message", Toast.LENGTH_LONG).show();
                            et_message.requestFocus();
                        }
                        else if(et_password.length() == 0)
                        {
                            Toast.makeText(getApplicationContext(), "Password cannot be null", Toast.LENGTH_LONG).show();
                            et_password.requestFocus();
                        }

                    }

                    else
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);

                }




            }
        });





    }


  

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes, 0,bytes.length);
        byte[] key =digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encrypt(String Data, String Password) throws Exception{
        SecretKeySpec key =generateKey(Password);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal =c.doFinal(Data.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal );
        return encryptedValue;
    }

    private void sendSMS(){
        String PhoneNo=et_phone.getText().toString().trim();
        String SMS=tempEncrypted;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(PhoneNo, null, SMS, null, null);
            Toast.makeText(getApplicationContext(),"Message sent :)",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Message not sent :')",Toast.LENGTH_LONG).show();
        }
    }


}
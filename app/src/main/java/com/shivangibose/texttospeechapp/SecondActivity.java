package com.shivangibose.texttospeechapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecondActivity extends AppCompatActivity {
    EditText inputText , number,outputText;
    TextView inputPassword;
    Button encBtn, decBtn ,backbtn, btnSend;
    String outputString,tempEncrypted,RecvPhnNo;
    String AES="AES";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        inputText=findViewById(R.id.inputText);
        inputPassword=findViewById(R.id.inputPassword);

        outputText=findViewById(R.id.textEncrypted);
//        encBtn=findViewById(R.id.btnEncrypt);
        decBtn=findViewById(R.id.btnDecrypt);
        backbtn=findViewById(R.id.btnLeave);
        btnSend=findViewById(R.id.btnSend);
        number=findViewById(R.id.phnNo);
        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
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



        backbtn.setOnClickListener(view -> {
            Intent intent =new Intent(SecondActivity.this,MainActivity.class);
            startActivity(intent);
        });

//        encBtn.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
                TelephonyManager telephonyManager= (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},3);
                }
//                inputPassword.setText(telephonyManager.getLine1Number());
                  inputPassword.setText("testpassword");




//        });

//        decBtn.setOnClickListener(view -> {
//
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    outputString=decrypt(outputString,RecvPhnNo);
//                    outputText.setText((outputString));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        });

        btnSend.setOnClickListener(view -> {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            outputString = encrypt(inputText.getText().toString(), inputPassword.getText().toString());
                        }
                        tempEncrypted = outputString;
//                        outputText.setText(outputString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendSMS();
                }

                else
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void receiveSMS() throws Exception {
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    for (SmsMessage sms:Telephony.Sms.Intents.getMessagesFromIntent(intent)){
//                        RecvPhnNo=sms.getOriginatingAddress();
                        RecvPhnNo=inputPassword.toString();
//                        outputText.setText(sms.getDisplayMessageBody());
                        Toast.makeText(getApplicationContext(),sms.getDisplayMessageBody(),Toast.LENGTH_LONG).show();

                    }

                }
            }
        };
        registerReceiver(br, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        SecretKeySpec key =generateKey(RecvPhnNo);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.getDecoder().decode(outputString);
        byte[] decValue=c.doFinal(decodedValue);
        String decryptedValue=new String(decValue);
        outputText.setText(decryptedValue);
    }


    private void sendSMS(){
        String PhoneNo=number.getText().toString().trim();
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

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private String decrypt(String outputString, String Password) throws Exception{
//        SecretKeySpec key =generateKey(Password);
//        Cipher c=Cipher.getInstance(AES);
//        c.init(Cipher.DECRYPT_MODE,key);
//        byte[] decodedValue = Base64.getDecoder().decode(outputString);
//        byte[] decValue=c.doFinal(decodedValue);
//        String decryptedValue=new String(decValue);
//        return decryptedValue;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encrypt(String Data, String Password) throws Exception{
        SecretKeySpec key =generateKey(Password);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal =c.doFinal(Data.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal );
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes, 0,bytes.length);
        byte[] key =digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
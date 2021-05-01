package com.example.text_to_speech_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    private Button speechEngine;
    private Button chatEncrption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechEngine = findViewById(R.id.speechEngine);
        speechEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TextToSpeechEngine.class));

            }
        });

        chatEncrption = findViewById(R.id.chatencrption);
        chatEncrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,EncryptedChatSystem.class));

            }
        });


    }
}
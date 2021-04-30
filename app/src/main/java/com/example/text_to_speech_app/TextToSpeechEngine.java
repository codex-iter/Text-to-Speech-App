package com.example.text_to_speech_app;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TextToSpeechEngine extends AppCompatActivity {

    TextToSpeech tts;
    int result;
    EditText input;
    Button speak, stop;
    String text;


    public TextToSpeechEngine() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech_engine);

        input = findViewById(R.id.et_input);
        speak = findViewById(R.id.bt_speak);
        stop = findViewById(R.id.bt_stop);


        tts =new TextToSpeech(TextToSpeechEngine.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if(i == TextToSpeech.SUCCESS){
                    result = tts.setLanguage(Locale.UK);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Feature not supported on your device", Toast.LENGTH_SHORT).show();
                }

            }
        });


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                {
                    Toast.makeText(getApplicationContext(), "Feature not supported on your device", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    text = input.getText().toString();
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);

                }

            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tts != null)
                {
                    tts.stop();
                }

            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(tts != null)
        {
            tts.stop();
            tts.shutdown();
        }

    }
}
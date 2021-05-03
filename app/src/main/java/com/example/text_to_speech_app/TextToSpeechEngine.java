package com.example.text_to_speech_app;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TextToSpeechEngine extends AppCompatActivity {

    TextToSpeech tts;
    int result;
    EditText input;
    Button speak, stop;
    String text;

    SeekBar speechRate;
    TextView tv_rate;

    float rate = 1.0f;
    float step = 0.02f;
    float min = 0.1f;



    public TextToSpeechEngine() {
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech_engine);

        input = findViewById(R.id.et_input);
        speak = findViewById(R.id.bt_speak);
        stop = findViewById(R.id.bt_stop);

        tv_rate = findViewById(R.id.tv_rate);

        tv_rate.setText("45");
//        tv_rate.setVisibility(View.VISIBLE);
        speechRate = findViewById(R.id.sb_rate);
        speechRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                rate = min+(progress*step);
                tts.setSpeechRate(rate);
                tv_rate.setText(Integer.toString(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        tts =new TextToSpeech(TextToSpeechEngine.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if(i == TextToSpeech.SUCCESS){
                    result = tts.setLanguage(Locale.UK);
                    tts.setSpeechRate(rate);
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
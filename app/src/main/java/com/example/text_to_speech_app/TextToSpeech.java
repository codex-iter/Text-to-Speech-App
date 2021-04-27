package com.example.text_to_speech_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TextToSpeech extends AppCompatActivity 
{

    TextToSpeech tts;
    int result;
    EditText input;
    Button speak, stop;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

         input = findViewById(R.id.et_input);
        speak = findViewById(R.id.bt_speak);
        stop = findViewById(R.id.bt_stop);
        
        tts =new TextToSpeech(TextToSpeechEngine.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) 
            {

                if(i == TextToSpeech.SUCCESS)
                {
                    result = tts.setLanguage(Locale.UK);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Feature not supported on your device", Toast.LENGTH_SHORT).show();
                }

            }
        });
        
        public void doSomething(View v)
        {
            switch (v.getId())
            {
                case R.id.speak:

                    if(result==TextToSpeech.LANG_NOT_SUPPORTED||result==TextToSpeech.LANG_MISSING_DATA)
                    {
                        Toast.makeText(getApplicationContext(), "Feature not supported in your device", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        text = et.getText().toString();
                        ttsobject.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                    }
                    break;

                case R.id.stop:

                    if(ttsobject!=null)
                    {
                        ttsobject.stop();
                    }
                    break;

           }
        }
        @Override
          protected void onDestroy() {
        super.onDestroy();
        if(ttsobject!=null)
        {
            ttsobject.stop();
            ttsobject.shutdown();
        }
    }
          
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
}

package com.example.text_to_speech_app;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TextToSpeechEngine extends AppCompatActivity {

    TextToSpeech tts;
    int result;
    EditText input;
    Button speak, stop;
    String text;

    SeekBar speechRate;
    TextView tv_rate;
    SeekBar speechPitch;
    TextView tv_pitch;

    float rate = 1.0f;
    float step = 0.02f;
    float min = 0.1f;

    float pitch = 1.0f;

    Locale[] locales = Locale.getAvailableLocales();
    List<Locale> localeList = new ArrayList<Locale>();
    List<String> country=  new ArrayList<String>();

    Locale currentSelection;

    Spinner language;
    ArrayAdapter<String> dataAdapter;

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

        speechRate = findViewById(R.id.sb_rate);
        speechPitch = findViewById(R.id.sb_pitch);

        language = findViewById(R.id.language);

        tv_rate = findViewById(R.id.tv_rate);
        tv_pitch = findViewById(R.id.tv_pitch);

        tv_rate.setText("45");
        tv_pitch.setText("45");


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


        speechPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                pitch = min+(progress*step);
                tts.setPitch(pitch);
                tv_pitch.setText(Integer.toString(progress));

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
                    tts.setPitch(pitch);

                    for (Locale locale : locales) {
                        int res = tts.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            localeList.add(locale);
                            country.add(locale.getDisplayName());
                        }
                    }
                    dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, country);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    language.setAdapter(dataAdapter);
                    int location2id = dataAdapter.getPosition(Locale.UK.getDisplayName());
                    language.setSelection(location2id);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Feature not supported on your device", Toast.LENGTH_SHORT).show();
                }

            }
        });

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                tts.setLanguage(localeList.get(position));
                currentSelection = localeList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                    TranslateAPI translateAPI = new TranslateAPI(Language.AUTO_DETECT,currentSelection.toString(),text);
                    translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                        @Override
                        public void onSuccess(String translatedText) {
                            text = translatedText;
                            tts.speak(translatedText, TextToSpeech.QUEUE_FLUSH,null);

                        }

                        @Override
                        public void onFailure(String ErrorText) {
                            Toast.makeText(getApplicationContext(),"Error fetching converted data", Toast.LENGTH_SHORT).show();
                        }
                    });


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

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
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
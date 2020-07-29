package com.avilpage.paymentspeaker;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speaker extends Activity {

    static TextToSpeech textToSpeech;
    private static Context context;

    public Speaker() {
        initTTS();
    }


    private void initTTS() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                }
            }
        });
    }

    public void speak(String speech) {
        if (speech == null || textToSpeech == null) {
            Log.e("--------", speech + textToSpeech);
            return;
        }
        Log.d("----------", speech);
        textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }
}


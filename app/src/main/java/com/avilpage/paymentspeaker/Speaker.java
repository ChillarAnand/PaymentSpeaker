package com.avilpage.paymentspeaker;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener {

    static TextToSpeech tts;
    private static Context context;

    public Speaker(Context context) {
        Speaker.context = context;
        Speaker.tts = new TextToSpeech(
                Speaker.context,
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.US);
                            speak("hello this is NH");
                        }
                    }
                });
    }

    @Override
    public void onInit(int status) {
        //check for successful instantiation

        if (status == TextToSpeech.SUCCESS) {
            if (tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(Locale.US);
            Log.e("TTS", "Initilization success!");

        } else if (status == TextToSpeech.ERROR) {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void speak(String speech) {
        if (speech == null || tts == null) {
            return;
        }
        Log.d("----------", speech);
        tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }
}


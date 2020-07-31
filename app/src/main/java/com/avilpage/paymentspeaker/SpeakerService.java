package com.avilpage.paymentspeaker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Locale;

public class SpeakerService extends Service implements TextToSpeech.OnInitListener {
    static TextToSpeech textToSpeech;
    public static final String MESSAGE = "message";
    public static final double speechRate = 0.9;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);

        MessageReceiver screenOnOffReceiver = new MessageReceiver();
        registerReceiver(screenOnOffReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        String message = intent.getStringExtra(SpeakerService.MESSAGE);
        if(message == null) {
            return START_STICKY;
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        speak(message, this);
        return START_STICKY;
    }

    @Override
    public void onInit(int status) {

    }

    public void speak(String speech, Context context) {
        if (textToSpeech == null) {
            initTTS(context);
        }
        int speechStatus = textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    private void initTTS(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
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
                    Log.d("TTS", "TTS failed");
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textToSpeech.setSpeechRate((float) speechRate);
    }


}
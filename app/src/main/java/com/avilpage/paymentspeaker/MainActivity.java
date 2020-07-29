package com.avilpage.paymentspeaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements MessageListener {
    private TextToSpeech textToSpeech;
    private Button testButton;
    private String parsedMessage;
    private String amount;
    private String amountInRupees;
    private static final Pattern amountPattern = Pattern.compile("\\d+\\.\\d{2} ");
    private Random random = new Random();
    private int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initTTS();
        setListeners();
        MessageReceiver.bindListener(this);
        startService(new Intent(this, SpeakerService.class));
    }

    private void setListeners() {
        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                randomNumber = random.nextInt(1000);
                String data = String.format("Credited. %s Rupees.", randomNumber);
                Log.i("TTS", "button clicked: " + data);
                speak(data);
            }

        });
    }

    public void speak(String speech) {
        if (textToSpeech == null) {
            initTTS();
        }
        int speechStatus = textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
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
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textToSpeech.setSpeechRate((float) 0.9);
    }

    private void checkPermissions() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    @Override
    public void messageReceived(String message) {
        parsedMessage = parse(message);
        if (parsedMessage == null) {
            return;
        }
        speak(parsedMessage);
    }

    private String parse(String message) {
        message = message.toLowerCase();
        message = message.replaceAll(",", "");
        if (!message.contains("paid") && !message.contains("credited")) {
            return null;
        }
        Matcher matcher = amountPattern.matcher(message);
        if (!matcher.find()) {
            return null;
        }
        amount = matcher.group(0);
        amountInRupees = amount.split("\\.")[0];
        parsedMessage = "Credited. " + amountInRupees + "rupees.";
        return parsedMessage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        textToSpeech.shutdown();
    }
}

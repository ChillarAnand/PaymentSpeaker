package com.avilpage.paymentspeaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private Button testButton;
    private Random random = new Random();
    private int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        setListeners();
        Intent backgroundService = new Intent(getApplicationContext(), SpeakerService.class);
        startService(backgroundService);
        Intent backgroundService2 = new Intent(getApplicationContext(), MessageReceiver.class);
        startService(backgroundService2);
    }

    private void setListeners() {
        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                randomNumber = random.nextInt(1000);
                String message = String.format("Credited. %s Rupees.", randomNumber);
                Context context = getApplicationContext();
                Intent serviceIntent = new Intent(context, SpeakerService.class);
                serviceIntent.putExtra(SpeakerService.MESSAGE, message);
                context.startService(serviceIntent);
            }

        });
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

}

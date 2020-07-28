package com.avilpage.paymentspeaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MessageListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.printf("------- main");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
        MessageReceiver.bindListener(this);
        new Speaker(getApplicationContext()).speak("hello this is NH");
    }

    @Override
    public void messageReceived(String message) {
        System.out.printf("main");
        System.out.printf(message);
        Toast.makeText(this, "New Message Received: " + message, Toast.LENGTH_SHORT).show();
    }

}

package com.avilpage.paymentspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageReceiver extends BroadcastReceiver {

    private static final Pattern amountPattern = Pattern.compile("\\d+\\.\\d{2} ");

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String messageBody = smsMessage.getMessageBody();
            String message = parse(messageBody);
            if (message != null) {
                Intent serviceIntent = new Intent(context, SpeakerService.class);
                serviceIntent.putExtra(SpeakerService.MESSAGE, message);
                context.startService(serviceIntent);
            }
        }
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
        String amount = matcher.group(0);
        String amountInRupees = amount.split("\\.")[0];
        String parsedMessage = "Credited. " + amountInRupees + " rupees.";
        return parsedMessage;
    }

}

package com.avilpage.paymentspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;


public class MessageReceiver extends BroadcastReceiver {

    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, SpeakerService.class);
        context.startService(serviceIntent);


        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String messageBody = smsMessage.getMessageBody();
//            String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
//                    + "Email From: " + smsMessage.getEmailFrom()
//                    + "Emal Body: " + smsMessage.getEmailBody()
//                    + "Display message body: " + smsMessage.getDisplayMessageBody()
//                    + "Time in millisecond: " + smsMessage.getTimestampMillis()
//                    + "Message: " + messageBody;
            if (messageBody != null) {
                mListener.messageReceived(messageBody);
            }
        }
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
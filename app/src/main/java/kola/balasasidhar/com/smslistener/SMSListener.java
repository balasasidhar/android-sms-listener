package kola.balasasidhar.com.smslistener;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SASi on 22-Dec-15.
 */
public class SMSListener extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();

            String senderNumber = "";
            String messageBody = "";
            String date = "";

            if (bundle != null) {

                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                for (int i = 0; i < messages.length; i++) {
                    SmsMessage message = messages[i];
                    senderNumber = message.getDisplayOriginatingAddress();
                    messageBody = message.getDisplayMessageBody();
                    date = getDate(message.getTimestampMillis());
                }

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle("New message from " + senderNumber)
                        .setContentText(messageBody)
                        .setSubText(date)
                        .setSmallIcon(R.drawable.ic_sms_white_24dp);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(002, mBuilder.build());
            }
        }
    }

    private String getDate(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm a");
            Date date = new Date(time);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

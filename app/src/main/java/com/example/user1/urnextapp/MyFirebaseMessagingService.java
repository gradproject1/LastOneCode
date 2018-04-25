package com.example.user1.urnextapp;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
// class for retrieve notification from fire base and create notification
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

// retrieve title and body from the notification in fire base
        String Message_Title =remoteMessage.getNotification().getTitle();
        String Message_Body = remoteMessage.getNotification().getBody();
        String Click_Action= remoteMessage.getNotification().getClickAction();

// store notification body
        String DataMessage = remoteMessage.getData().get("Message");
        String DataForm = remoteMessage.getData().get("From");

// create the notification with sound and light
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(Message_Title)
                .setContentText(Message_Body)
                .setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

// to open specific page when click on the notification
        Intent resultIntent= new Intent(Click_Action);
        resultIntent.putExtra("Message",DataMessage);
        resultIntent.putExtra("From",DataForm);

        PendingIntent resultPendingIntent=
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

// to give each notification unique id and notification manager
        int notification_id= (int) System.currentTimeMillis();
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notification_id,builder.build());
    }
}
package com.example.user1.urnextapp;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nony on 28/03/18.
 */

public class MyBeacon extends Application {
static int flag=0;
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());


        /////////////////////////////////////////

        // add this below:
        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {
                flag=1;
                showNotification(
                        "Enter",
                        "welcome to the clinic!");
            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                // could add an "exit" notification too if you want (-:
                flag=0;
                showNotification(
                        "Exit",
                        "Goodbey!");
            }
        });

        ///////////////////////////////////////
        // add this below:
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        6632, 59107));
            }
        });
        //////////////////////////////////////////
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, WelcomePage.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.beacon_gray)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}
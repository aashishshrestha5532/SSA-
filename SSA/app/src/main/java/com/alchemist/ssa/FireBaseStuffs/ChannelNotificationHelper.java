package com.alchemist.ssa.FireBaseStuffs;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Created by SUSON on 1/29/2018.
 */

public class ChannelNotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID="com.ssa.FireBase";
    private static final String CHANNEL_NAME="Event Notifications";
    private NotificationManager manager;

    public ChannelNotificationHelper(Context base) {
        super(base);
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        initializeChannel();

    }

    private void initializeChannel() {
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channel);
    }

    public Notification.Builder getNotificationBuilder() {
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID);
    }

    public void displayNotification(int id,Notification.Builder builder) {
        manager.notify(id,builder.build());
    }
}

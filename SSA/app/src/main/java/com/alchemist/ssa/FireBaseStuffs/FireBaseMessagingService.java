package com.alchemist.ssa.FireBaseStuffs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alchemist.ssa.EventStuffs.Event;
import com.alchemist.ssa.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ashish Alton on 7/20/2018.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0) {


            String title, image_url, message;
            Log.d("Mytoken", remoteMessage.toString());

            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");
            image_url = remoteMessage.getData().get("image_url");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ChannelNotificationHelper helper = new ChannelNotificationHelper(this);
                helper.displayNotification(101, helper.getNotificationBuilder().setContentTitle(title).setContentText(message).setAutoCancel(true).setSmallIcon(R.drawable.money));
            } else {

                Intent intent = new Intent(this, Event.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle(title);
                builder.setContentText(message);
                builder.setContentIntent(pendingIntent);
                builder.setSmallIcon(R.drawable.money);
                builder.setSound(uri);
                //image is not recieveing because the data section does not conatin the image
                ImageRequest request = new ImageRequest(image_url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(0, builder.build());
                    }
                }, 0, 0, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
                });
            }
        }
    }
}

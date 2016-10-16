package com.booxtown.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.booxtown.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.booxtown.R;

/**
 * Created by duong on 9/20/2016.
 */
public class FireBaseService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String ss= remoteMessage.getNotification().getBody();
        sendNotification(remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("position","1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.icon_buy);
        Bitmap thumb=Bitmap.createBitmap(100,132, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(thumb);
        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                new Rect(0,0,thumb.getWidth(),thumb.getHeight()),null);
        Drawable drawable = new BitmapDrawable(getResources(),thumb);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_buy)
                .setLargeIcon(((BitmapDrawable)drawable).getBitmap())
                .setContentTitle("Booxtown Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

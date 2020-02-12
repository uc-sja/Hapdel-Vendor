package com.utility.hapdelvendor.Service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.utility.hapdelvendor.Activity.MainActivity;
import com.utility.hapdelvendor.Activity.NotificationActivity;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.LocalStorage;
import java.util.Date;

import static android.text.TextUtils.isEmpty;

public class ParentNotificationService extends FirebaseMessagingService {
    private static final String TAG = "ParentNotifyService";
    private static final String CHANNEL_1_ID = "channe1";
    private static final String CHANNEL_2_ID = "channe2";


    public static int general_notification_count = 0;
    public static int ride_notification_count = 0;


    private NotificationManager notificationManager;
    private MainActivity mainActivity;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        LocalStorage.setNotificationToken(s);
        LocalStorage.setIsNotificationRereshed(true);
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: gen "+ general_notification_count+"  "+ ride_notification_count+"  local ride"+LocalStorage.getRideNotificationCount()+"  "+LocalStorage.getNotificationCount());
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);

        //notication count is initialized at onCreate which runs for only once at start of activity
        //so that we can count all the unread notifications after the activity is retarted

        general_notification_count = LocalStorage.getNotificationCount();
        ride_notification_count = LocalStorage.getRideNotificationCount();

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, " : "+remoteMessage.toString());
        Intent intent = new Intent("MyData");
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getData().get("distance"));
        intent.putExtra("jobId", remoteMessage.getData().get("body"));
        intent.putExtra("distance", remoteMessage.getData().get("distance"));
        intent.putExtra("price", remoteMessage.getData().get("price"));
        /*

        broadcast is used to communicate between service and activity as
        we cannot access any field of activity(even if its public static)
        from the service directly

        */

        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), remoteMessage.getData().get("distance"), remoteMessage.getData().get("price"));

        broadcaster.sendBroadcast(intent); // sending broadcast after shownotification method because,  value of i is increased
        // in shownotification method. And when onReceive method of broadcast receiver
        // is activated in mainactivity it accesses the value of i
    }


    private void showNotification(String title, String body, String distance, String price) {
        //ride notification
        PendingIntent pendingIntent;
        Boolean rideNotification = false;
        if(body != null && !isEmpty(body) && distance!=null && !isEmpty(distance) && price != null && !isEmpty(price)){
            ride_notification_count++;
            rideNotification = true;
            LocalStorage.setRideNotificationCount(ride_notification_count);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("jobId", body);
            intent.putExtra("distance", distance);
            intent.putExtra("price", price);

            Log.d(TAG, "showNotification: "+body+" " +distance+" "+price);

            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            //general notification
        } else {

            rideNotification = false;

            general_notification_count++;
            LocalStorage.setNotificationCount(general_notification_count);
            Intent intent = new Intent(this, NotificationActivity.class);
            Log.d(TAG, "showNotification: "+body+" " +distance+" "+price);

            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_small_png);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Tap to know more");
        bigText.setBigContentTitle(title);
        bigText.setSummaryText("New Task");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_address)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setStyle(bigText);

        //a random integer that never gets repeated
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        notificationManager =  (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //        MainActivity.bottomNavigation.setNotification(i+"", 2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "Your_channel_id";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Channel human reable", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);

            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();

        //Checkking if notification is for ride or general notification;
        if(rideNotification){
            Log.d(TAG, "showNotification: ride notification ");
            Uri ride_sound = Uri.parse("android.resource://"+ getApplicationContext().getPackageName()+"/"+R.raw.apx_tone_alert);
            try {
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ride_sound);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Uri general = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            try {
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), general);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        notificationManager.notify(m,  notification);

    }
}
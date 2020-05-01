package com.tppa.morsecode;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.content.ContextCompat.getSystemService;

public class MorseCodeLedManager {
    NotificationReceiver nReceiver;


    private NotificationManagerCompat notificationManager;
    final private String channelIDLights = "Lights";
    private Context context;
    private int unit;
    private long[] pattern;

    int notificationID = 1;
    int receivedID;

    public MorseCodeLedManager(int unit,long[] pattern, Context context){
        this.context = context;
        this.unit = unit;
        this.pattern = pattern;

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tppa.morsecode.GET_ACTIVE_NOTIFICATIONS_LIST");
        this.context.registerReceiver(nReceiver,filter);

    }


    public void setUnit(int unit){
        this.unit = unit;
    }

    public void setPattern(long[] pattern){
        this.pattern = pattern;
    }

    public void createLedNotification() {
        notificationID = 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, channelIDLights);

        NotificationManager notificationManager =  (NotificationManager) this.context.getSystemService( Context.NOTIFICATION_SERVICE );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(channelIDLights, "Lights notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.enableLights(true);
            //notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            //notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder.setSmallIcon(R.drawable.notification_icon1);
        builder.setContentTitle("Morse code");
        builder.setContentText("Led notification");
        builder.setLights(Color.MAGENTA, 500, 500);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_MAX);

        notificationManager.notify(notificationID, builder.build());

    }



    public void stopNotification(){

        Intent i = new Intent("com.tppa.morsecode.GET_ACTIVE_NOTIFICATIONS_LIST_SERVICE");
        this.context.sendBroadcast(i);
        if(notificationID == receivedID){
            notificationManager.cancel(notificationID);
        }
    }

    public void blinkForCharacter(String character){
        //actions for blinking character
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            receivedID = intent.getIntExtra("id", -1);
        }
    }

    public void onDestroy() {
        this.context.unregisterReceiver(nReceiver);
    }
}

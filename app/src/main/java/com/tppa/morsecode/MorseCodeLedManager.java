package com.tppa.morsecode;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.content.ContextCompat.getSystemService;

public class MorseCodeLedManager {

    final String CHANNEL_ID_Lights = "Lights";
    NotificationManagerCompat notificationManager;
    int notificationId;
    Context context;

    public MorseCodeLedManager(Context context){
        this.context = context;
    }

    //must modify for led
    public void createLedNotification() {
        Intent intent = new Intent(context, MorseCodeLedManager.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder;

        createNotificationChannel();

        builder = new NotificationCompat.Builder(context, CHANNEL_ID_Lights);

        builder.setSmallIcon(R.drawable.notification_icon1)
                .setContentTitle("Morse Code")
                .setContentText("Led started")
                .setLights(Color.MAGENTA, 2000, 200)
                .setDefaults(NotificationCompat.DEFAULT_SOUND| NotificationCompat.FLAG_SHOW_LIGHTS)
                .setContentIntent(pendingIntent);

        notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("Vibration channel");
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_Lights, name, importance);
            channel.enableLights(true);
            //channel.setLightColor(Color.MAGENTA);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


    public void stopNotification(int notificationId){

        notificationManager.cancel(notificationId);
    }

    public void blinkForCharacter(String character){
        //actions for blinking character
    }
}

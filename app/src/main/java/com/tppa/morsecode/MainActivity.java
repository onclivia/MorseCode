package com.tppa.morsecode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    MorseCodeVibrationManager morseCodeVibrationManager;

    final String CHANNEL_ID_Lights = "Lights";
    final String CHANNEL_ID_Vibration = "Vibration";
    NotificationManagerCompat notificationManager;

    ImageButton btnON;
    ImageButton btnOFF;
    //MorseCodeLedManager morseCodeManager;
    long[] vibrationPattern = new long[] {};
    final int checkLedAndVibrationNotificationId = 0;
    final int frequencyNotificationId = 1;
    int unit = 0;
    private SeekBar seekBar;
    private TextView progress_textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //morseCodeLedManager = new MorseCodeLedManager(this, vibrationPattern);

        btnON = findViewById(R.id.check_led_btn_on);
        btnOFF = findViewById(R.id.check_led_btn_off);

        seekBar = findViewById(R.id.frequency_seekBar);
        progress_textView = findViewById(R.id.seekBar_progress);
        progress_textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());


        unit = seekBar.getProgress();
        vibrationPattern = new long[] {0, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress_textView.setText(pval+"/"+seekBar.getMax());
                unit = pval;
                vibrationPattern = new long[] {300, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};
                morseCodeVibrationManager.createVibration(vibrationPattern, 0);
                //morseCodeLedManager.setVibrationPattern(vibrationPattern);
            }
        });


        morseCodeVibrationManager = new MorseCodeVibrationManager(unit, this);


    }

    public void checkNotificationButtonClicked(View view){

        //make ON button visible

        morseCodeVibrationManager.createVibration(vibrationPattern, 0);
        btnOFF.setVisibility(View.GONE);
        btnON.setVisibility(View.VISIBLE);
    }

    public void checkNotificationButtonUnclicked(View view) {

        //notificationManager.cancel(checkLedAndVibrationNotificationId);
        morseCodeVibrationManager.stopVibration();
        btnON.setVisibility(View.GONE);
        btnOFF.setVisibility(View.VISIBLE);
    }



    public void checkLedAndVibrationNotification() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder;

        createNotificationChannel();

        builder = new NotificationCompat.Builder(context, CHANNEL_ID_Vibration);

        vibrationPattern = new long[] {0, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};
        builder.setSmallIcon(R.drawable.notification_icon1)
                .setContentTitle("Morse Code")
                .setContentText("Led started")
                .setVibrate(vibrationPattern)
                .setLights(Color.MAGENTA, 2000, 200)
                .setDefaults(NotificationCompat.DEFAULT_SOUND|  NotificationCompat.FLAG_SHOW_LIGHTS)
                .setOngoing(true)
                .setContentIntent(pendingIntent);

        notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(checkLedAndVibrationNotificationId, builder.build());
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("Vibration channel");

            //vibrationPattern = new long[] {0, 500, 500, 500,400, 500, 300};
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_Vibration, name, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            //channel.setVibrationPattern(vibrationPattern);
            //channel.setLightColor(Color.MAGENTA);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


    public void writeSentenceActivity(View view){
        Intent intent = new Intent(getBaseContext(),   WriteSentenceActivity.class);
        intent.putExtra("unit",unit);
        startActivity(intent);
    }

    public void goToAlphabetActivity(View view){
        Intent intent = new Intent(getBaseContext(),   GoToAlphabetActivity.class);
        intent.putExtra("unit",unit);
        startActivity(intent);
    }

    public void decryptSentenceActivity(View view){
        Intent intent = new Intent(getBaseContext(),   DecryptSentenceActivity.class);
        intent.putExtra("unit",unit);
        startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        checkNotificationButtonUnclicked(findViewById(R.id.check_led_btn_on));
        Log.d("onPause","onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("onResume", "onResume");
    }

    @Override
    protected void onStop(){
        super.onStop();
        checkNotificationButtonUnclicked(findViewById(R.id.check_led_btn_on));
        Log.d("onStop", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        checkNotificationButtonUnclicked(findViewById(R.id.check_led_btn_on));
        Log.d("onDestroy", "onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("","onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("","onSaveInstanceState");
    }



}

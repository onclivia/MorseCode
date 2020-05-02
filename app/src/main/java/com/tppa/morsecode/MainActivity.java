package com.tppa.morsecode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    MorseCodeGeneralManager morseCodeGeneralManager;
    int choice;

    ImageButton btnON;
    ImageButton btnOFF;
    ImageButton choose;

    //MorseCodeLedManager morseCodeManager;
    long[] pattern = new long[] {};
    int unit = 0;
    private SeekBar seekBar;
    private TextView progress_textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnON = findViewById(R.id.check_led_btn_on);
        btnOFF = findViewById(R.id.check_led_btn_off);
        choose = findViewById(R.id.choose);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseLedOrVibration();
            }
        });


        seekBar = findViewById(R.id.frequency_seekBar);
        progress_textView = findViewById(R.id.seekBar_progress);
        progress_textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());


        unit = seekBar.getProgress();
        pattern = new long[] {0, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};

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
                pattern = new long[] {300, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};
                morseCodeGeneralManager.setUnit(unit);
                morseCodeGeneralManager.setPattern(pattern);
                checkNotificationButtonClicked(findViewById(R.id.check_led_btn_on));
            }
        });

        morseCodeGeneralManager = new MorseCodeGeneralManager(choice, unit, pattern, context);
    }

    public void checkNotificationButtonClicked(View view){

        //make ON button visible
        morseCodeGeneralManager.createActionForCheckButton();
        btnOFF.setVisibility(View.GONE);
        btnON.setVisibility(View.VISIBLE);
    }

    public void checkNotificationButtonUnclicked(View view) {

        morseCodeGeneralManager.stopActionForCheckButton();
        btnON.setVisibility(View.GONE);
        btnOFF.setVisibility(View.VISIBLE);
    }


    public String[] items = new String[]{"Vibrations","LED"};
    public void chooseLedOrVibration(){


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose:");
        builder.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice = i;
                morseCodeGeneralManager.setChoice(choice);
                Toast.makeText(MainActivity.this,"Your choice: " + items[i], Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    public void writeSentenceActivity(View view){
        Intent intent = new Intent(getBaseContext(),   WriteSentenceActivity.class);
        intent.putExtra("unit",unit);
        intent.putExtra("pattern",pattern);
        intent.putExtra("choice",choice);
        startActivity(intent);
    }

    public void goToAlphabetActivity(View view){
        Intent intent = new Intent(getBaseContext(),   GoToAlphabetActivity.class);
        intent.putExtra("unit",unit);
        intent.putExtra("pattern",pattern);
        intent.putExtra("choice",choice);
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
        morseCodeGeneralManager.morseCodeLedManager.onDestroy();
        checkNotificationButtonUnclicked(findViewById(R.id.check_led_btn_off));
        Log.d("onDestroy", "onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        choice = savedInstanceState.getInt("CHOICE_STATE");
        morseCodeGeneralManager.setChoice(choice);
        progress_textView.setText(savedInstanceState.getInt("PROGRESS")+"/"+seekBar.getMax());
        Log.d("","onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("CHOICE_STATE", choice);
        outState.putInt("PROGRESS", unit);
        Log.d("","onSaveInstanceState");
    }



}

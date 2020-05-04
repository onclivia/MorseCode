package com.tppa.morsecode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    MorseCodeGeneralManager morseCodeGeneralManager;
    int choice;

    Switch startStopSwitch;
    ImageButton choose;

    //MorseCodeLightManager morseCodeManager;
    long[] pattern = new long[] {};
    int unit = 0;
    private SeekBar seekBar;
    private TextView progress_textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startStopSwitch = findViewById(R.id.startStopSwitch);
        startStopSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startSwitch();
                } else {
                    // The toggle is disabled
                    stopSwitch();
                }
            }
        });


        choose = findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseLightOrVibration();
            }
        });
        choice = 0;

        seekBar = findViewById(R.id.frequency_seekBar);
        progress_textView = findViewById(R.id.seekBar_progress);
        progress_textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());


        unit = seekBar.getProgress();
        pattern = new long[] {0, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress_textView.setText(progressVal +"/"+seekBar.getMax());
                unit = progressVal;
                pattern = new long[] {0, unit, unit, 3*unit, unit, unit, unit, 3*unit, unit};
                morseCodeGeneralManager.setUnit(unit);
                morseCodeGeneralManager.setPattern(pattern);
                startStopSwitch.setChecked(true);
                //startSwitch();
            }
        });

        morseCodeGeneralManager = new MorseCodeGeneralManager(choice, unit, pattern, context);
    }

    public void startSwitch(){
        morseCodeGeneralManager.createActionForCheckButton();

    }

    public void stopSwitch() {
        morseCodeGeneralManager.stopActionForCheckButton();
    }


    public String[] items = new String[]{"Flashlight","Vibration"};
    public void chooseLightOrVibration(){


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose:");
        builder.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(choice != i){
                    startStopSwitch.setChecked(false);
                    //stopSwitch();
                    choice = i;
                    morseCodeGeneralManager.setChoice(choice);
                    Toast.makeText(MainActivity.this,"Your choice: " +i + ": " + items[i], Toast.LENGTH_LONG).show();

                }
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
        stopSwitch();
        startStopSwitch.setChecked(false);
        morseCodeGeneralManager.stopLightAndVibration();
        Log.d("stopLightAndVibration","stopLightAndVibration");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("onResume", "onResume");
    }

    @Override
    protected void onStop(){
        super.onStop();
        stopSwitch();
        startStopSwitch.setChecked(false);
        morseCodeGeneralManager.stopLightAndVibration();
        Log.d("release", "release");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        morseCodeGeneralManager.stopLightAndVibration();
        stopSwitch();
        startStopSwitch.setChecked(false);

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

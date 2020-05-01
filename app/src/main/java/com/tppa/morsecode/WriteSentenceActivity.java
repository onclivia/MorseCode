package com.tppa.morsecode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Normalizer;
import java.util.Objects;

public class WriteSentenceActivity extends AppCompatActivity {

    MorseCodeGeneralManager morseCodeGeneralManager;
    Context context;
    public String sentence = "";
    public String encryptedSentence = "";
    Alphabet alphabet;
    TextView character_show;
    TextView morse_code_show;
    EditText sentence_edit ;

    int unit = 0;
    int index = 0;
    int delay = unit;
    long[] pattern = new long[]{};
    int choice = 0;
    Handler handler = new Handler();
    AlertDialog.Builder builder;

    String activityState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sentence);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            unit = extras.getInt("unit");
            choice = extras.getInt("choice");
            pattern = extras.getLongArray("pattern");

        }

        if (savedInstanceState != null) {
            activityState = savedInstanceState.getString("ACTIVITY_STATE_KEY");
        }


        context = this;
        morseCodeGeneralManager = new MorseCodeGeneralManager(choice, unit, pattern, context);
        alphabet = new Alphabet();
        builder = new AlertDialog.Builder(this);

        character_show = findViewById(R.id.character_show);
        morse_code_show = findViewById(R.id.morse_code_for_character_show);
        sentence_edit = findViewById(R.id.writeSentenceEdit);


        //Get Morse Code actions

        FloatingActionButton getMorseCodeBtn = findViewById(R.id.getMorseCodeBtn);
        getMorseCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentence = "";
                if(sentence_edit.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Write a sentence...", Toast.LENGTH_SHORT).show();
                }
                else{
                    sentence = sentence_edit.getText().toString();
                    getMorseCode();
                }
                try{
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e, Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


    public void getMorseCode(){
        encryptedSentence= "";
        delay = unit;
        index = 1;
        handler.removeCallbacks(showCharacter);
        handler.removeCallbacks(startChooseActionDialogDialog);
        handler.postDelayed(showCharacter,delay);

    }


    Runnable startChooseActionDialogDialog = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.choose_action);
            builder.setMessage(R.string.choose_action_message);
            builder.setCancelable(false);
            builder.setPositiveButton("Send Sms", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getBaseContext(),   SendSms.class);
                    intent.putExtra("encrypted",encryptedSentence);
                    intent.putExtra("sentence",sentence_edit.getText().toString());
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Write again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    sentence_edit.setText("");
                    character_show.setText("");
                    morse_code_show.setText("");
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };


    Runnable showCharacter = new Runnable() {

        @Override
        public void run() {

            String character;
            String morseCode;

            //normalize for diacritics
            sentence = sentence.toLowerCase();
            String s = Normalizer.normalize(sentence, Normalizer.Form.NFD);
            s = s.replaceAll("[^\\p{ASCII}]", "");
            sentence = s;

            delay = 0;
            character = sentence.substring(index-1,index);
            if (alphabet.alphabet.containsKey((character))) {
                morseCode = alphabet.alphabet.get(character);
                encryptedSentence = encryptedSentence + morseCode + " ";
                morseCodeGeneralManager.actionForSingleCharacter(character);
                character_show.setText(character);
                morse_code_show.setText(morseCode);
                for(int j = 0; j < morseCode.length(); j++){
                    if(morseCode.charAt(j) == '•')
                        delay+=unit;
                    else if(morseCode.charAt(j) == '-')
                        delay+=3*unit;
                }

            } else if (character.equals(" ")) {
                encryptedSentence = encryptedSentence + " ";
                delay = 2*unit;
            }
            delay+=10*unit;

            index++;

            if(index <= sentence.length()){
                handler.postDelayed(showCharacter,delay);
            }
            else {
                handler.postDelayed(startChooseActionDialogDialog, 500);
            }
        }

    };



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(showCharacter);
        handler.removeCallbacks(startChooseActionDialogDialog);
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(showCharacter);
        handler.removeCallbacks(startChooseActionDialogDialog);
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(showCharacter);
        handler.removeCallbacks(startChooseActionDialogDialog);
        Log.d("lifecycle","onDestroy invoked");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        //super.onRestoreInstanceState(savedInstanceState);
        character_show.setText(savedInstanceState.getString("CHARACTER_SHOW_KEY"));
        morse_code_show.setText(savedInstanceState.getString("MORSE_CODE_SHOW_KEY"));
        sentence_edit.setText(savedInstanceState.getString("EDIT_SENTENCE_KEY"));
        choice = savedInstanceState.getInt("CHOICE_STATE");
        Log.d("","onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putString("ACTIVITY_STATE_KEY", activityState);
        outState.putString("CHARACTER_SHOW_KEY", character_show.getText().toString());
        outState.putString("MORSE_CODE_SHOW_KEY", morse_code_show.getText().toString());
        outState.putString("EDIT_SENTENCE_KEY",sentence_edit.getText().toString() );
        outState.putInt("CHOICE_STATE", choice);
        super.onSaveInstanceState(outState);
        Log.d("","onSaveInstanceState");
    }

}

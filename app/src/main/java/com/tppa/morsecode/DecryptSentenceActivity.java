package com.tppa.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DecryptSentenceActivity extends AppCompatActivity {

    EditText encrypted_edit;
    TextView decrypted_sentence;
    Button decryptBtn;

    String encrypted_code;
    Alphabet alphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_sentence);

        encrypted_edit = findViewById(R.id.encrypted_edit);
        decrypted_sentence = findViewById(R.id.decrypted_sentence);
        decryptBtn = findViewById(R.id.decryptBtn);
        alphabet = new Alphabet();

        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encrypted_edit.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Write encrypted code...", Toast.LENGTH_SHORT).show();
                }
                else{
                    encrypted_code = encrypted_edit.getText().toString();
                    decryptCode();
                }
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    View focusedView = getCurrentFocus();

                    if (focusedView != null) {
                        inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });

    }

    public void decryptCode(){
        String decrypted = "";
        String[] morse_code_words;
        String[] morse_code_word;
        String character;

        if(encrypted_code.contains(".")){
            encrypted_code = encrypted_code.replaceAll("\\.","•");
        }
        encrypted_code = encrypted_code.trim();

        morse_code_words = encrypted_code.split(" {2}");

        for(String word : morse_code_words){
            morse_code_word = word.split(" ");
            for( String morse_code_character : morse_code_word){
                if(alphabet.morse_code_alphabet.containsKey(morse_code_character)){
                    character = alphabet.morse_code_alphabet.get(morse_code_character);
                    decrypted += character;
                }
                else{
                    Toast.makeText(this,"No result. Please write valid morse code encryption. Check alphabet again.", Toast.LENGTH_LONG).show();
                }
            }
            decrypted += " ";
        }

        decrypted =  decrypted.substring(0, 1).toUpperCase() + decrypted.substring(1);
        decrypted_sentence.setText(decrypted);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("stopLightAndVibration", "stopLightAndVibration");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("release", "release");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "onDestroy");
    }


}
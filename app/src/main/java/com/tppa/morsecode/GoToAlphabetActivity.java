package com.tppa.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class GoToAlphabetActivity extends AppCompatActivity {

    MorseCodeVibrationManager morseCodeVibrationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int unit = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            unit = extras.getInt("unit");
        }

        setContentView(R.layout.activity_go_to_alphabet);

        morseCodeVibrationManager = new MorseCodeVibrationManager(unit, this);
        final Alphabet alphabet = new Alphabet();
        final AlphabetAdapter alphabetAdapter = new AlphabetAdapter(this, alphabet.alphabet);

        GridView gridView = findViewById(R.id.alphabet_grid_view);
        gridView.setAdapter(alphabetAdapter);
        gridView.animate();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                String key = alphabet.alphabet.keyAt(position);
                String value = alphabet.alphabet.get(key);
                showMorseCodeForCharacter(key, value);

                // This tells the GridView to redraw itself
                // in turn calling your BooksAdapter's getView method again for each cell
                alphabetAdapter.notifyDataSetChanged();

                ColorDrawable[] colors = {
                        new ColorDrawable(Color.DKGRAY), // Animation starting color
                        new ColorDrawable(Color.LTGRAY) // Animation ending color
                };

                // Initialize a new transition drawable instance
                TransitionDrawable transitionDrawable = new TransitionDrawable(colors);

                // Set the clicked item background
                view.setBackground(transitionDrawable);

                // Finally, Run the item background color animation
                // This is the grid view item click effect
                transitionDrawable.startTransition(600); // 600 Milliseconds
            }
        });

    }



    public void showMorseCodeForCharacter(String key, String value){
        Toast toast = Toast.makeText(this, "Character: "+ key + "\nMorse code: "+ value, Toast.LENGTH_SHORT);
        toast.show();

        morseCodeVibrationManager.vibrateForCharacter(key);
        //JUST MAKE LED BLINKING!!
    }


    @Override
    protected void onPause(){
        super.onPause();
        morseCodeVibrationManager.stopVibration();
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
        morseCodeVibrationManager.stopVibration();
        Log.d("onStop", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy", "onDestroy");
    }


}

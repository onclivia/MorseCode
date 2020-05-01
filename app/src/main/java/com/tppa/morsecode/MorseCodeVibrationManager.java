package com.tppa.morsecode;

import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MorseCodeVibrationManager {

    Vibrator vibrator ;
    Alphabet alphabet;
    int unit;
    long[] pattern;
    Context context;

    public MorseCodeVibrationManager(int unit, long[] pattern, Context context){
        this.unit = unit;
        this.pattern = pattern;
        this.context = context;
        vibrator  = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        alphabet = new Alphabet();
    }


    public void setUnit(int unit){
        this.unit = unit;
    }

    public void setPattern(long[] pattern){
        this.pattern = pattern;
    }

    public void createVibration(int repeatMode){
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(this.pattern, repeatMode);
        }
    }

    public void stopVibration(){
        vibrator.cancel();
    }


    public void vibrateForCharacter(String character){
        String morseCode = alphabet.alphabet.get(character);
        char morseCodeCharacter;
        int dot = unit;
        int dash = 3*unit;
        ArrayList<Integer> s = new ArrayList<>();
        s.add(0);

        for(int i=0; i < morseCode.length(); i++){
            morseCodeCharacter = morseCode.charAt(i);
            if (morseCodeCharacter == '•'){
                    s.add(dot);
                    s.add(dot);//for pattern vibrationOff
                }
            else if (morseCodeCharacter == '-'){
                    s.add(dash);
                    s.add(dot);//for pattern vibrationOff
                }
            }

        long[] pattern = new long[s.size()];
        for (int i = 0; i < s.size(); i++)
            pattern[i] = s.get(i);

        if (vibrator.hasVibrator()){
            vibrator.vibrate(pattern, -1);
        }


    }

}

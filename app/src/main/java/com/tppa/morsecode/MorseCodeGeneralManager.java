package com.tppa.morsecode;

import android.content.Context;

public class MorseCodeGeneralManager {

    MorseCodeVibrationManager morseCodeVibrationManager;
    MorseCodeLedManager morseCodeLedManager;
    int choice;
    long[] pattern;
    int unit;

    public MorseCodeGeneralManager(int choice, int unit, long[] pattern, Context context){
        this.choice = choice;
        this.pattern = pattern;
        this.unit = unit;
        this.morseCodeVibrationManager = new MorseCodeVibrationManager(unit,context);
        this.morseCodeLedManager = new MorseCodeLedManager(unit, context);
    }

    public void setChoice(int choice){
        this.choice = choice;
    }


    public void setUnit(int unit){
        morseCodeVibrationManager.setUnit(unit);
        morseCodeLedManager.setUnit(unit);
        this.unit = unit;
    }

    public void createActionForCheckButton(){
        if(choice == 0){
            morseCodeVibrationManager.createVibration(pattern, 0);
        }
        else if(choice == 1){
            morseCodeLedManager.createLedNotification();
        }
    }

    public void stopActionForCheckButton(){
        if(choice == 0){
            morseCodeVibrationManager.stopVibration();
        }
        else if(choice == 1){
            morseCodeLedManager.stopNotification();
        }
    }

    public void actionForSingleCharacter(String character){
        if(choice == 0){
            morseCodeVibrationManager.vibrateForCharacter(character);
        }
        else if(choice == 1){
            morseCodeLedManager.blinkForCharacter(character);
        }
    }
}

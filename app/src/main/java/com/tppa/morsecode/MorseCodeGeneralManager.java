package com.tppa.morsecode;

import android.content.Context;

public class MorseCodeGeneralManager {

    MorseCodeVibrationManager morseCodeVibrationManager;
    MorseCodeLightManager morseCodeLightManager;
    int choice;
    long[] pattern;
    int unit;

    public MorseCodeGeneralManager(int choice, int unit, long[] pattern, Context context){
        this.choice = choice;
        this.pattern = pattern;
        this.unit = unit;
        this.morseCodeVibrationManager = new MorseCodeVibrationManager(unit,pattern, context);
        this.morseCodeLightManager = new MorseCodeLightManager(unit, pattern, context);
    }

    public void setChoice(int choice){
        this.choice = choice;
    }


    public void setUnit(int unit){
        morseCodeVibrationManager.setUnit(unit);
        morseCodeLightManager.setUnit(unit);
        this.unit = unit;
    }

    public void setPattern(long[] pattern){
        morseCodeVibrationManager.setPattern(pattern);
        morseCodeLightManager.setPattern(pattern);
        this.pattern = pattern;
    }



    public void createActionForCheckButton(){

        if(choice == 0) {
            morseCodeLightManager.createLight();
        }
        else if(choice == 1){
            morseCodeVibrationManager.createVibration(0);
        }
    }

    public void stopActionForCheckButton(){

        if(choice == 0) {
            morseCodeLightManager.turnOffFlashLight();
        }
        else if(choice == 1){
            morseCodeVibrationManager.stopVibration();
        }
    }

    public void actionForSingleCharacter(String character){
        if(choice == 0){
            morseCodeLightManager.flashForCharacter(character);
        }
        else if(choice == 1){
            morseCodeVibrationManager.vibrateForCharacter(character);
        }
    }

    public void stopLightAndVibration(){
        morseCodeVibrationManager.stopVibration();
        morseCodeLightManager.release();
    }


}

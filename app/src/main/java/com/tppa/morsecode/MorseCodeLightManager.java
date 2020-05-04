package com.tppa.morsecode;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class MorseCodeLightManager {
    private Context context;
    private int unit;
    private long[] pattern;
    private long[] temporaryPattern;

    private Camera camera;
    private static final int CAMERA_REQUEST = 123;

    private boolean isFlashOn;
    private boolean hasCameraFlash;
    android.hardware.Camera.Parameters params;

    private Handler handler = new Handler();
    int index;
    Alphabet alphabet;

    public MorseCodeLightManager(int unit, long[] pattern, Context context){
        this.context = context;
        this.unit = unit;
        this.pattern = pattern;
        this.camera = null;

        this.hasCameraFlash = this.context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        this.isFlashOn = false;
        this.alphabet = new Alphabet();
    }


    public void setUnit(int unit){
        this.unit = unit;
    }

    public void setPattern(long[] pattern){
        this.pattern = pattern;
    }

    public void createLight() {

        if (!this.hasCameraFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog.Builder alert = new AlertDialog.Builder(this.context);
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            Dialog dialog = alert.create();
            dialog.show();
        }

        else{
            getCamera();
            this.isFlashOn = true;
            handler.removeCallbacks(blinkContinuous);
            index = 0;
            handler.postDelayed(blinkContinuous, pattern[0]);
        }
    }

    public void turnOnFlashLight(){
        CameraManager cameraManager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);

        try {
            this.isFlashOn = true;
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);

        } catch (CameraAccessException e) {
        }
    }

    public void turnOffFlashLight() {

        if (this.isFlashOn) {
            CameraManager cameraManager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);
            try {
                this.isFlashOn = false;
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, false);


            } catch (CameraAccessException e) {
            }

        }
    }

    Runnable blinkContinuous = new Runnable() {
        @Override
        public void run() {
            long delay = 0;
            if(isFlashOn){

                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (index % 2 != 0) {
                    try {
                        delay = pattern[index];
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, true);
                    } catch (CameraAccessException e) {
                    }

                } else {
                    try {
                        delay = 2 * pattern[index];
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, false);
                    } catch (CameraAccessException e) {
                    }
                }

                index++;
                if(index < pattern.length){
                    handler.postDelayed(this, delay);
                }
                else if(index == pattern.length){
                    index = 1;
                    handler.postDelayed(this,delay);
                }
            }
        }

    };

    private void getCamera() {

        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this.context, Manifest.permission.CAMERA)){
                Toast.makeText(this.context,"Camera permission allows us to Access flash", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions((Activity) this.context,new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            }
        }
    }



    void release(){

        handler.removeCallbacks(blinkContinuous);
        handler.removeCallbacks(blinkForCharacter);
        if (this.camera != null) {
            this.isFlashOn = false;
            this.camera.release();
            this.camera = null;
        }

    }

    private Runnable blinkForCharacter = new Runnable() {


        @Override
        public void run() {

            if(index < temporaryPattern.length){
                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (index % 2 != 0) {
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, true);
                    } catch (CameraAccessException e) {
                    }

                }
                else {
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, false);
                    } catch (CameraAccessException e) {
                    }
                }

                handler.postDelayed(this, temporaryPattern[index]);
            }
            index++;
        }
    };


    public void flashForCharacter(String character){
        this.index = 0;
        String morseCode = alphabet.alphabet.get(character);
        char morseCodeCharacter;
        int dot = this.unit;
        int dash = 3*this.unit;
        ArrayList<Integer> s = new ArrayList<>();
        s.add(0);

        assert morseCode != null;
        for(int i = 0; i < morseCode.length(); i++){
            morseCodeCharacter = morseCode.charAt(i);
            if (morseCodeCharacter == '•'){
                s.add(dot);
                s.add(dot * 2);//for pattern vibrationOff
            }
            else if (morseCodeCharacter == '-'){
                s.add(dash);
                s.add(dot * 2);//for pattern vibrationOff
            }
        }

        temporaryPattern = new long[s.size()];
        for (int i = 0; i < s.size(); i++)
            temporaryPattern[i] = s.get(i);

        handler.removeCallbacks(blinkForCharacter);
        handler.postDelayed(blinkForCharacter,100);


    }



}

package com.tppa.morsecode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SendSms extends AppCompatActivity {

    public String encryptedSentence;
    public String sentence;
    public String phone_number;

    EditText phone_number_edit;
    TextView contact_name;

    Intent intent;

    public int MY_PERMISSIONS_REQUEST_SEND_SMS;
    public int RequestPermissionCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            encryptedSentence = extras.getString("encrypted");
            sentence = extras.getString("sentence");
        }

        EnableRuntimeContactPermission();

        phone_number_edit = findViewById(R.id.phone_number);
        TextView sentence_text_view = findViewById(R.id.sentence);
        TextView encrypted_sentence_text_view = findViewById(R.id.encrypted_sentence);
        contact_name = findViewById(R.id.contact_name);

        sentence_text_view.setText(sentence);
        encrypted_sentence_text_view.setText(encryptedSentence);

        FloatingActionButton getMorseCodeBtn = findViewById(R.id.getMorseCodeBtn);
        getMorseCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone_number_edit.getText().toString().isEmpty()){
                    phone_number = "";
                    Toast.makeText(getApplicationContext(),
                            "Write a phone number or search contact...", Toast.LENGTH_SHORT).show();
                }
                else{
                    phone_number = phone_number_edit.getText().toString();
                    sendSMSMessage();
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

        Button searchBtn = findViewById(R.id.contactBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);
            }
        });


    }

    public void EnableRuntimeContactPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS))
        {
            Toast.makeText(this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    assert cursor1 != null;
                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                contact_name.setText(TempNameHolder);
                                phone_number_edit.setText(TempNumberHolder);
                                phone_number = TempNumberHolder;
                            }
                        }

                    }
                }
                break;
        }
    }



    public void sendSMSMessage() {
        String sms = encryptedSentence;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Toast.makeText(this,"SMS permission allows us to send sms.", Toast.LENGTH_LONG).show();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            try {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(phone_number, null, sms, null, null);
                Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "SMS Failed to Send: "+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
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
        Log.d("onStop", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy", "onDestroy");
    }


}

package a8.group.ttnm.x.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.RecognizeServiceManager.SpeechRecognizerManager;

public class PhoneCalling extends AppCompatActivity implements View.OnClickListener,SpeechRecognizerManager.OnGoogleResultListener,SpeechRecognizerManager.OnPocketResultListener {

    ArrayList<String> listOptions;
    ImageButton dialNum0, dialNum1, dialNum2, dialNum3, dialNum4, dialNum5, dialNum6, dialNum7, dialNum8, dialNum9, dialDelete;
    EditText txtNumber;
    ListView listOption;
    FloatingActionButton dialCall;
    String number = "";
    SpeechRecognizerManager speechRecognizerManager ;
    private static final String PHONE_CALLING = "phone calling" ;

    private void init() {
        dialNum0 = (ImageButton) findViewById(R.id.dial_num_0);
        dialNum0.setOnClickListener(this);
        dialNum1 = (ImageButton) findViewById(R.id.dial_num_1);
        dialNum1.setOnClickListener(this);
        dialNum2 = (ImageButton) findViewById(R.id.dial_num_2);
        dialNum2.setOnClickListener(this);
        dialNum3 = (ImageButton) findViewById(R.id.dial_num_3);
        dialNum3.setOnClickListener(this);
        dialNum4 = (ImageButton) findViewById(R.id.dial_num_4);
        dialNum4.setOnClickListener(this);
        dialNum5 = (ImageButton) findViewById(R.id.dial_num_5);
        dialNum5.setOnClickListener(this);
        dialNum6 = (ImageButton) findViewById(R.id.dial_num_6);
        dialNum6.setOnClickListener(this);
        dialNum7 = (ImageButton) findViewById(R.id.dial_num_7);
        dialNum7.setOnClickListener(this);
        dialNum8 = (ImageButton) findViewById(R.id.dial_num_8);
        dialNum8.setOnClickListener(this);
        dialNum9 = (ImageButton) findViewById(R.id.dial_num_9);
        dialNum9.setOnClickListener(this);
        dialDelete = (ImageButton) findViewById(R.id.btnDeleteNumber);
        dialDelete.setOnClickListener(this);
        dialCall = (FloatingActionButton) findViewById(R.id.fabCalling);
        dialCall.setOnClickListener(this);

        txtNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        listOption = (ListView) findViewById(R.id.listOptions);

        speechRecognizerManager = new SpeechRecognizerManager(this,PHONE_CALLING);
        speechRecognizerManager.setOnGoogleResultListener(this);
        speechRecognizerManager.setOnPocketResultListener(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_calling);
        init();
        disableEditText(txtNumber);
    }
    @Override
    public void onPause(){
        isInBackground = true ;
        if(speechRecognizerManager != null && speechRecognizerManager.mPocketSphinxRecognizer != null)
            speechRecognizerManager.mPocketSphinxRecognizer.cancel();
        super.onPause();
    }
    boolean isInBackground = false ;
    @Override
    public void onStop(){
        isInBackground = true ;
        if(speechRecognizerManager != null && speechRecognizerManager.mPocketSphinxRecognizer != null)
            speechRecognizerManager.mPocketSphinxRecognizer.cancel();
        super.onStop();
    }
    @Override
    public void onResume(){
        if(isInBackground){
            try{
                speechRecognizerManager.setPocketListening(PHONE_CALLING);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view != R.id.btnDeleteNumber) {
            switch (number.length()) {
                case 4:
                    number = number + "-";
                    break;
                case 8:
                    number = number + "-";
                    break;
                case 12:
                    number = number + "-";
                    break;
            }
        }
        switch (view) {
            case R.id.fabCalling:
                phoneCall();
                break;
            case R.id.dial_num_0:
                number = number + "0";
                break;
            case R.id.dial_num_1:
                number = number + "1";
                break;
            case R.id.dial_num_2:
                number = number + "2";
                break;
            case R.id.dial_num_3:
                number = number + "3";
                break;
            case R.id.dial_num_4:
                number = number + "4";
                break;
            case R.id.dial_num_5:
                number = number + "5";
                break;
            case R.id.dial_num_6:
                number = number + "6";
                break;
            case R.id.dial_num_7:
                number = number + "7";
                break;
            case R.id.dial_num_8:
                number = number + "8";
                break;
            case R.id.dial_num_9:
                number = number + "9";
                break;
            case R.id.btnDeleteNumber:
                if(number.length() <= 0) break ;
                StringBuilder tempNumber = new StringBuilder(number);
                if(tempNumber.charAt(tempNumber.length()-1) == '-'){
                    tempNumber.replace(tempNumber.length()-2,tempNumber.length(),"");
                }
                else tempNumber.replace(tempNumber.length()-1,tempNumber.length(),"");
                number = tempNumber.toString();
                break;
        }
        txtNumber.setText(number);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnGoogleResult(String result) {
        try {
            Toast.makeText(this,"Bạn nói : " + result,Toast.LENGTH_SHORT).show();
            number = result ;
            txtNumber.setText(result);
            //splitToNumber(result);
        }catch (Exception e){
            Toast.makeText(this,"Bạn nói không phải là một số : " + result,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        speechRecognizerManager.setPocketListening(PHONE_CALLING);
    }

    /*public void splitToNumber(String result){
        String[] strNumber = result.split(" ");
        for(int i = 0 ; i < strNumber.length ; i++){
            char c = convertStringtoNumber(strNumber[i]);
            if(c == 'c') continue;
            number = number + c;
        }
        txtNumber.setText(number);
    }

    public char convertStringtoNumber(String number){
        switch (number){
            case "không": return '0';
            case "một": return '1';
            case "hai": return '2';
            case "ba": return '3';
            case "bốn": return '4';
            case "năm": return '5';
            case "sáu": return '6';
            case "bảy": return '7';
            case "tám": return '8';
            case "chín": return '9';
        }
        return 'c' ;
    }*/

    private void phoneCall(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number.replaceAll("-", "")));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    private void enterNumber(char c){
            if(number.length() > 16) return ;
            switch (number.length()) {
                case 4:
                    number = number + "-";
                    break;
                case 8:
                    number = number + "-";
                    break;
                case 12:
                    number = number + "-";
                    break;
            }
            number += c ;
        txtNumber.setText(number);
    }


    private void deleteNumber(){
        if(number.length() <= 0) return ;
        StringBuilder tempNumber = new StringBuilder(number);
        if(tempNumber.charAt(tempNumber.length()-1) == '-'){
            tempNumber.replace(tempNumber.length()-2,tempNumber.length(),"");
        }
        else tempNumber.replace(tempNumber.length()-1,tempNumber.length(),"");
        number = tempNumber.toString();
        //Log.d("HUYNHafterDelete",number);
        txtNumber.setText(number);
    }

    @Override
    public void OnPocketResult(String result) {
        switch(result){
            case "zero":
                enterNumber('0');
                break;
            case "one":
                enterNumber('1');
                break;
            case "two":
                enterNumber('2');
                break;
            case "three":
                enterNumber('3');
                break;
            case "four":
                enterNumber('4');
                break;
            case "five":
                enterNumber('5');
                break;
            case "six":
                enterNumber('6');
                break;
            case "seven":
                enterNumber('7');
                break;
            case "eight":
                enterNumber('8');
                break;
            case "nine":
                enterNumber('9');
                break;
            case "undo":
                deleteNumber();
                break;
            case "clear":
                txtNumber.setText("");
                number = "" ;
                break;
            case "call":
                phoneCall();
                break;
            case "back":
                try{
                    finish();
                    speechRecognizerManager.mPocketSphinxRecognizer.cancel();
                    speechRecognizerManager.mPocketSphinxRecognizer.shutdown();
                }catch (Exception e){
                    Log.d("HUYNH","calling back error");
                }finally {
                }
                return;
            case "google":
                //splitToNumber("một hai ba bốn năm sáu năm");
                speechRecognizerManager.setGoogleListening();
                return;

        }
        speechRecognizerManager.setPocketListening(PHONE_CALLING);
    }
}

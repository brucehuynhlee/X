package a8.group.ttnm.x.controller.Test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import a8.group.ttnm.x.controller.RecognizeServiceManager.SpeechRecognizerManager;
import a8.group.ttnm.x.view.MainApp;

public class IncomingCallReceiver extends BroadcastReceiver implements SpeechRecognizerManager.OnGoogleResultListener,SpeechRecognizerManager.OnPocketResultListener{
    private static final String MENU_CALL = "menu call";
    public IncomingCallReceiver() {

    }
    private SpeechRecognizerManager speechRecognizerManager ;
    public static String TAG="IncomingCallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        //speechRecognizerManager = new SpeechRecognizerManager(context,MENU_CALL);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.d(TAG,"IncomingCallReceiver**Call State=" + state);

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG,"IncomingCallReceiver**Idle");
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Intent mainIntent = new Intent(context, MainApp.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                // Incoming call
                String incomingNumber =
                        intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG,"IncomingCallReceiver**Incoming call " + incomingNumber);

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.d(TAG,"IncomingCallReceiver **Offhook");
            }
        } else if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG,"IncomingCallReceiver **Outgoing call " + outgoingNumber);

            setResultData(null); // Kills the outgoing call

        } else {
            Log.d(TAG,"IncomingCallReceiver **unexpected intent.action=" + intent.getAction());
        }
    }

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d(TAG,"IncomingCallReceiver **" + ex.toString());
            return false;
        }
        return true;
    }

    @Override
    public void OnGoogleResult(String result) {

    }
    boolean isDecline = false ;
    @Override
    public void OnPocketResult(String result) {
        if(result.equals("Answer")){
            isDecline = false ;
        }else if(result.equals("decline")){
            isDecline = true ;
        }



    }
}

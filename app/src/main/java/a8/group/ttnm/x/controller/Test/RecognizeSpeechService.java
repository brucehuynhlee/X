package a8.group.ttnm.x.controller.Test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RecognizeSpeechService extends Service {

    private static final String TAG = "RECOGNIZE_SPEECH_SERVICE" ;

    protected static AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

    protected static boolean mIsListening;
    protected static volatile boolean mIsCountDownOn;
    private static boolean mIsStreamSolo;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        Log.i(TAG,"onCreate");
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<RecognizeSpeechService> mtarget;

        IncomingHandler(RecognizeSpeechService target)
        {
            mtarget = new WeakReference<RecognizeSpeechService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final RecognizeSpeechService target = mtarget.get();

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        // turn off beep sound  
                        if (!mIsStreamSolo)
                        {
                            mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                            mIsStreamSolo = true;
                        }
                    }
                    if (!target.mIsListening)
                    {
                        target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                        target.mIsListening = true;
                        Log.d(TAG, "message start listening"); //$NON-NLS-1$
                    }
                    break;

                case MSG_RECOGNIZER_CANCEL:
                    if (mIsStreamSolo)
                    {
                        mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                        mIsStreamSolo = false;
                    }
                    target.mSpeechRecognizer.cancel();
                    target.mIsListening = false;
                    //Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
                    break;
            }
        }
    }

    // Count down timer for Jelly Bean work around
    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish()
        {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {
              
            }
        }
    };

    @Override
    public void onDestroy()
    {
        Log.i(TAG,"onDestroy");
        super.onDestroy();

        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            // speech input will be processed, so there is no need for count down anymore
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
            Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            //Log.d(TAG, "onEndOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try
            {
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }
            Log.d(TAG, "error = " + error); //$NON-NLS-1$
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
            Log.i(TAG,partialResults.describeContents()+"");
        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();

            }
            Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(result!=null) {  //when speaking timeout happen, results is null

                // matches are the return values of speech recognition engine

                if (result.size() > 0) {
                    Log.d(TAG, "onResults : " + result.get(0).toString()); //$NON-NLS-1$
                }

            }
            Log.d(TAG, "onResults"); //$NON-NLS-1$

        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

    }
}
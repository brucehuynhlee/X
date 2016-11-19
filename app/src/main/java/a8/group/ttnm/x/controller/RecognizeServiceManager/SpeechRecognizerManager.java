package a8.group.ttnm.x.controller.RecognizeServiceManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Created by LittleDragon on 11/18/2016.
 */

public class SpeechRecognizerManager {


    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "main";
    private static final String MENU = "menu";
    private static final String SEARCH = "search";
    private static final String CONTACTS = "contact";
    private static final String CONTACT_OPTION = "contact option";
    private static final String CONTACT_OPTION_DELETE_CONFIRM = "delete confirm";
    private static final String PHONE_CALLING = "phone calling" ;
    private String startCommand ;


    public edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer;
    protected android.speech.SpeechRecognizer mGoogleSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    private static final String TAG = SpeechRecognizerManager.class.getSimpleName();
    private Context mContext;


    public SpeechRecognizerManager(Context context,String startCommand) {
        this.mContext = context;
        this.startCommand  = startCommand ;
        initGoogleSpeechRecognizer();
        initPockerSphinx();
    }

    public void setPocketListening(String keyWord){
        mGoogleSpeechRecognizer.stopListening();
        mPocketSphinxRecognizer.startListening(keyWord);

    }
    public void setGoogleListening(){
        mPocketSphinxRecognizer.cancel();
        mGoogleSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    private void initGoogleSpeechRecognizer(){

        mGoogleSpeechRecognizer = android.speech.SpeechRecognizer
                .createSpeechRecognizer(mContext);
        mGoogleSpeechRecognizer.setRecognitionListener(new GoogleRecognitionListener());
        mSpeechRecognizerIntent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

    }

    private void initPockerSphinx() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();


                    //Creates a new speech recognizer builder with default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();

                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    // Threshold to tune for keyphrase to balance between false alarms and misses
                    speechRecognizerSetup.setKeywordThreshold(1e-45f);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    // Create keyword-activation search.
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
                    // Create grammar-based search for selection between demos
                    File menuGrammar = new File(assetDir, "menu.gram");
                    mPocketSphinxRecognizer.addGrammarSearch(MENU, menuGrammar);
                    File searchGrammar = new File(assetDir, "search.gram");
                    mPocketSphinxRecognizer.addGrammarSearch(SEARCH, searchGrammar);
                    File contactsGrammar = new File(assetDir, "contacts.gram");
                    mPocketSphinxRecognizer.addGrammarSearch(CONTACTS, contactsGrammar);
                    File contactOptionGrammar = new File(assetDir, "contact_option.gram");
                    mPocketSphinxRecognizer.addGrammarSearch(CONTACT_OPTION, contactOptionGrammar);
                    File contactDeleteConfirmGrammar = new File(assetDir, "contact_delete_confirm.gram");
                    mPocketSphinxRecognizer.addGrammarSearch(CONTACT_OPTION_DELETE_CONFIRM, contactDeleteConfirmGrammar);

                    // calling gram
                    File phoneCallingGrammar = new File(assetDir, "phone_calling.gram");
                    mPocketSphinxRecognizer.addGrammarSearch(PHONE_CALLING, phoneCallingGrammar);

                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(mContext, "Failed to init pocketSphinxRecognizer:"+result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Sẵn sàng để sử dụng giọng nói", Toast.LENGTH_SHORT).show();
                    restartSearch(startCommand);
                }
            }
        }.execute();

    }


    private void restartSearch(String searchName) {
        mPocketSphinxRecognizer.stop();
        mPocketSphinxRecognizer.startListening(searchName);

    }

    // interface for live
    public interface OnPocketResultListener
    {
        public void OnPocketResult(String result);
    }
    public interface OnGoogleResultListener{
        public void OnGoogleResult(String result);
    }

    public OnPocketResultListener onPocketResultListener ;
    public OnGoogleResultListener onGoogleResultListener ;

    // set listener
    public void setOnPocketResultListener(OnPocketResultListener onResultListener){
        onPocketResultListener = onResultListener;
    }
    public void setOnGoogleResultListener(OnGoogleResultListener onResultListener){
        onGoogleResultListener = onResultListener;
    }


    public class PocketSphinxRecognitionListener implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        /**
         * In partial result we get quick updates about current hypothesis. In
         * keyword spotting mode we can react here, in other modes we need to wait
         * for final result in onResult.
         */
        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null)
                return;


            String text = hypothesis.getHypstr();
            Toast.makeText(mContext,"Bạn nói :"+text,Toast.LENGTH_SHORT).show();
            // bắt đầu
            mPocketSphinxRecognizer.cancel();
            onPocketResultListener.OnPocketResult(text);
        }

        @Override
        public void onResult(Hypothesis hypothesis) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onTimeout() {

        }
    }




    protected class GoogleRecognitionListener implements
            android.speech.RecognitionListener {

        boolean performingSpeechSetup ;

        private final String TAG = GoogleRecognitionListener.class
                .getSimpleName();

        @Override
        public void onBeginningOfSpeech() {
            performingSpeechSetup = true;
        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            performingSpeechSetup = false ;
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onError(int error) {
            Log.e(TAG, "onError:" + error);

            if (performingSpeechSetup && (error == android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT
                    || error == android.speech.SpeechRecognizer.ERROR_NO_MATCH)) {

                mPocketSphinxRecognizer.startListening(MENU);

            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }
        @Override
        public void onResults(Bundle results) {
            if ((results != null)
                    && results.containsKey(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)) {
                ArrayList<String> heard =
                        results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                float[] scores = results.getFloatArray(android.speech.SpeechRecognizer.CONFIDENCE_SCORES);

                for (int i = 0; i < heard.size(); i++) {
                    Log.d(TAG, "onResultsheard:" + heard.get(i)
                            + " confidence:" + scores[i]);

                }

                onGoogleResultListener.OnGoogleResult(heard.get(0));
            }
            // continue listening pocket to trigger (after last stop)
            //mPocketSphinxRecognizer.startListening(KWS_SEARCH);


        }



        @Override
        public void onEvent(int eventType, Bundle params) {

        }

    }

}
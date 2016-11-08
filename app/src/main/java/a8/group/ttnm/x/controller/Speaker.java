package a8.group.ttnm.x.controller;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by LittleDragon on 11/7/2016.
 */
public class Speaker implements android.speech.tts.TextToSpeech.OnInitListener {
    private TextToSpeech tts;

    private boolean ready = false;

    private boolean allowed = false;

    public Speaker(Context context){
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            tts.setLanguage(Locale.ENGLISH);
            ready = true;
        }else{
            ready = false;
        }
    }
}

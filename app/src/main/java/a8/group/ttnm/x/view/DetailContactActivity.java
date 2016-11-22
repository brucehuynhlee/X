package a8.group.ttnm.x.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.RecognizeServiceManager.SpeechRecognizerManager;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFavorite;

public class DetailContactActivity extends AppCompatActivity implements SpeechRecognizerManager.OnPocketResultListener,SpeechRecognizerManager.OnGoogleResultListener{
    Contact contact = null ;
    private static final String MENU_DETAIL_CONTACT = "detail contact";
    TextView detailName , detailNumber , detailMail , detailAddress ;
    ImageButton addFavorite ;
    boolean check = false ;
    SpeechRecognizerManager speechRecognizerManager ;
    private void init(){
        speechRecognizerManager = new SpeechRecognizerManager(this,MENU_DETAIL_CONTACT);
        speechRecognizerManager.setOnPocketResultListener(this);
        speechRecognizerManager.setOnGoogleResultListener(this);
         detailAddress = (TextView)findViewById(R.id.detailAddress);
         detailMail = (TextView)findViewById(R.id.detailMail);
         detailName = (TextView)findViewById(R.id.detailName);
         detailNumber = (TextView)findViewById(R.id.detailNumber);
         addFavorite = (ImageButton)findViewById(R.id.addFavorite);
         addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeContact();
            }
         });
    }

    private void likeContact(){
        if(check){
            check = false ;
            addFavorite.setImageResource(android.R.drawable.star_big_off);
            ContactsFavorite.getInstanceContactsFavorite(getApplicationContext()).removeContact(contact);
        }else{
            check = true ;
            addFavorite.setImageResource(android.R.drawable.star_big_on);
            ContactsFavorite.getInstanceContactsFavorite(getApplicationContext()).favoriteContacts.add(contact);
        }
    }
    private void setContent(){
        if(contact != null){
            detailNumber.setText(contact.getNumberContact());
            detailName.setText(contact.getNameContact());
            detailMail.setText(contact.getEmailContact());
            detailAddress.setText(contact.getAddressContact());
            if(ContactsFavorite.getInstanceContactsFavorite(this.getApplication()).checkContact(contact)){
                addFavorite.setImageResource(android.R.drawable.star_big_on);
                check = true ;
            }else addFavorite.setImageResource(android.R.drawable.star_big_off);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        setTitle("Chi tiết liên hệ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
        Intent intent = getIntent();
        contact = intent.getParcelableExtra("contactDetail");
        setContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnGoogleResult(String result) {

    }

    @Override
    public void OnPocketResult(String result) {
            switch (result){
                case "like":
                    likeContact();
                    speechRecognizerManager.setPocketListening(MENU_DETAIL_CONTACT);
                    break;
                case "back":
                    speechRecognizerManager.mPocketSphinxRecognizer.cancel();
                    finish();
                    break;
                default: speechRecognizerManager.setPocketListening(MENU_DETAIL_CONTACT);
                    break;
            }
    }
}

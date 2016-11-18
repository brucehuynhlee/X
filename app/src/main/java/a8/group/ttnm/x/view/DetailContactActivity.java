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
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFavorite;

public class DetailContactActivity extends AppCompatActivity {
    Contact contact = null ;
    TextView detailName , detailNumber , detailMail , detailAddress ;
    ImageButton addFavorite ;
    private void init(){
         detailAddress = (TextView)findViewById(R.id.detailAddress);
         detailMail = (TextView)findViewById(R.id.detailMail);
         detailName = (TextView)findViewById(R.id.detailName);
         detailNumber = (TextView)findViewById(R.id.detailNumber);
         addFavorite = (ImageButton)findViewById(R.id.addFavorite);
         addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite.setImageResource(android.R.drawable.star_big_on);
                ContactsFavorite.getInstanceContactsFavorite(getApplicationContext()).favoriteContacts.add(contact);
            }
         });
    }

    private void setContent(){
        if(contact != null){
            detailNumber.setText(contact.getNumberContact());
            detailName.setText(contact.getNameContact());
            detailMail.setText(contact.getEmailContact());
            detailAddress.setText(contact.getAddressContact());
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
}

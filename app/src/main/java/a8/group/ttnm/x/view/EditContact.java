package a8.group.ttnm.x.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;


import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.net.URISyntaxException;
import java.util.ArrayList;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.Unity;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFactory;

public class EditContact extends AppCompatActivity implements View.OnClickListener{

    String filePath = "" ;
    String[] groupContacts = {"Home","Nhà hàng","Khách hàng"};
    MaterialBetterSpinner spinnerMaterial ;
    FloatingActionButton fab ;
    ImageView imageView ;
    EditText nameContact ,  numberContact , emailContact , addressContact ;
    TextInputLayout layoutName , layoutEmail , layoutNumber ;
    Uri uriProfile ;
    static final int FILE_CHOOSER = 100 ;

    private void init(){
        fab = (FloatingActionButton) findViewById(R.id.fabChooseImage);
        imageView = (ImageView)findViewById(R.id.imageView);
        nameContact = (EditText)findViewById(R.id.contact_name);
        numberContact = (EditText)findViewById(R.id.contact_number);
        emailContact = (EditText)findViewById(R.id.contact_email);
        addressContact = (EditText)findViewById(R.id.contact_address);
        spinnerMaterial = (MaterialBetterSpinner) findViewById(R.id.contact_group);

        layoutNumber = (TextInputLayout) findViewById(R.id.layoutNumber);
        layoutNumber.setError("Bạn nhập vào không phải số");
        layoutName = (TextInputLayout) findViewById(R.id.layoutName);
        layoutName.setError("Bạn nhập vào không phải một tên");
        layoutEmail = (TextInputLayout) findViewById(R.id.layoutEmail);
        layoutEmail.setError("Bạn nhập vào không phải địa chỉ Email");
    }

    private void setListener(){
        fab.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        init();
        setListener();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(android.R.drawable.arrow_up_float);
        // Set button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ArrayAdapter<String> adapterGroup = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,groupContacts);
        spinnerMaterial.setAdapter(adapterGroup);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.saveContact :
                saveContact();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fabChooseImage:
                showUploadImage();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER
                && resultCode == Activity.RESULT_OK && data != null) {
            uriProfile = data.getData();
            if (uriProfile != null) {
                try {
                    imageView.setImageURI(uriProfile);
                    filePath = Unity.getPath(this,uriProfile);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    filePath = "";
                }
            }
        }
    }

    private void showUploadImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_CHOOSER);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            /*Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    private void saveContact(){
        Contact contact = new Contact();
        contact.setIdContact(12);
        contact.setNumberContact(numberContact.getText().toString());
        contact.setNameContact(nameContact.getText().toString());
        contact.setUriImageContact(Unity.getUriToDrawable(this,R.mipmap.profile));
        //Log.i("HUYNH",uriProfile.toString());
        ContactsFactory.getInstanceContactsFactory(this.getApplicationContext()).contact.add(contact);
    }





}

package a8.group.ttnm.x.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.net.URISyntaxException;
import java.util.ArrayList;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.Unity;

public class EditContact extends AppCompatActivity implements View.OnClickListener{

    String filePath = "" ;
    String[] groupContacts = {"Home","Nhà hàng","Khách hàng"};
    MaterialBetterSpinner spinnerMaterial ;
    FloatingActionButton fab ;
    ImageView imageView ;
    static final int FILE_CHOOSER = 100 ;

    private void init(){
        fab = (FloatingActionButton) findViewById(R.id.fabChooseImage);
        imageView = (ImageView)findViewById(R.id.imageView);
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

        spinnerMaterial = (MaterialBetterSpinner) findViewById(R.id.android_material_design_spinner);
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fabChooseImage:
                showUploadImage();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER
                && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    imageView.setImageURI(uri);
                    filePath = Unity.getPath(this,uri);
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



}

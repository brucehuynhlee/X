package a8.group.ttnm.x.view;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import a8.group.ttnm.x.controller.AutoCompleteContactAdapter;
import a8.group.ttnm.x.controller.PagerAdapter;
import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.RecognizeServiceManager.SpeechRecognizerManager;
import a8.group.ttnm.x.controller.Test.RecognizeSpeechService;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFactory;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class MainApp extends AppCompatActivity implements SpeechRecognizerManager.OnGoogleResultListener,SpeechRecognizerManager.OnPocketResultListener{

    private static final String CONTACT_FRAGMENT = "menu" ;
    private static final int REQUEST_RECOGNIZE = 10000 ;

    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    AutoCompleteTextView autoContact ;
    AutoCompleteContactAdapter autoContactAdapter ;
    List<Contact> contacts ;
    ViewPager viewPager ;

    // kernel recognize speech
    SpeechRecognizerManager mSpeechRecognize ;
    private static final String MENU = "menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        // init recognize speech virtual service
        mSpeechRecognize = new SpeechRecognizerManager(this);
        mSpeechRecognize.setOnGoogleResultListener(this);
        mSpeechRecognize.setOnPocketResultListener(this);

        contacts = ContactsFactory.getInstanceContactsFactory(this).contact ;
        Log.d("HUYNH","contact size : " + contacts.size());
        autoContact = (AutoCompleteTextView)findViewById(R.id.autoListContacts);
        autoContact.setThreshold(1);
        autoContactAdapter = new AutoCompleteContactAdapter(this,R.layout.activity_main_app,contacts);
        autoContact.setAdapter(autoContactAdapter);
        autoContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailContactActivity.class);
                intent.putExtra("contactDetail",contacts.get(position));
                startActivity(intent);
            }
        });
        // hide keyboard
        hideInputSoft();
        // device admin
        /*try {
            // Initiate DevicePolicyManager.
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, DeviceAdmin.class);

            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                // mDPM.lockNow();
                // Intent intent = new Intent(MainActivity.this,
                // TrackDeviceService.class);
                // startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.ic_menu_recent_history).setText("Lịch sử"));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.ic_menu_call).setText("Danh bạ"));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.stat_notify_chat).setText("Hộp thư thoại"));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.star_big_on).setText("Yêu thích"));
        //tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.ic_menu_gallery).setText("Nhóm"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideInputSoft();
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public void hideInputSoft(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isInBackground = false ;
    @Override
    public void onPause(){
        super.onPause();
        isInBackground = true ;
    }
    @Override
    public void onStop(){
        super.onStop();
        isInBackground = true ;
    }
    @Override
    public void onResume(){
        super.onResume();
        //hideInputSoft();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_RECOGNIZE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    autoContact.setText(result.get(0));
                }
                break;
            }
        }

    }

    @Override
    public void OnGoogleResult(String result) {
            autoContact.setText(result);
            mSpeechRecognize.setPocketListening("search");
    }

    @Override
    public void OnPocketResult(String result) {
           switch (result){
               case "search":
                   autoContact.setCursorVisible(true);
                   autoContact.setText("");
                   mSpeechRecognize.setGoogleListening();
                   return;
               case "history":
                   viewPager.setCurrentItem(0);
                   break;
               case "contact":
                   viewPager.setCurrentItem(1);
                   break;
               case "chat":
                   viewPager.setCurrentItem(2);
                   break;
               case "favorite":
                   viewPager.setCurrentItem(3);
                   break;
           }
        mSpeechRecognize.setPocketListening(MENU);

    }



    /*private void switchCommand(String searchName) {
        recognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(CONTACT_FRAGMENT)){
            recognizer.startListening(searchName,10000);
        }
        else
            recognizer.startListening(searchName, 10000);

    }*/

}

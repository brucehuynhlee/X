package a8.group.ttnm.x.view;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.List;

import a8.group.ttnm.x.controller.AutoCompleteContactAdapter;
import a8.group.ttnm.x.controller.PagerAdapter;
import a8.group.ttnm.x.R;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFactory;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class MainApp extends AppCompatActivity implements RecognitionListener{

    //Test recognize speech
    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    private static final String CONTACT_FRAGMENT = "menu" ;

    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    AutoCompleteTextView autoContact ;
    AutoCompleteContactAdapter autoContactAdapter ;
    List<Contact> contacts ;
    ViewPager viewPager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
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

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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


        //run thread
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(MainApp.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    switchCommand(CONTACT_FRAGMENT);
                }
            }
        }.execute();
    }


    public void hideInputSoft(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }

    private void switchFragment(String txt){
        switch (txt){
            case "digits":
                viewPager.setCurrentItem(1);
                break;
        }
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
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        //switchFragment(text);
        if(text.equals("contact")){
            viewPager.setCurrentItem(1);
        }
        switchCommand(CONTACT_FRAGMENT);

    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {

    }

    private void switchCommand(String searchName) {
        recognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(CONTACT_FRAGMENT)){
            recognizer.startListening(searchName,10000);
        }
        else
            recognizer.startListening(searchName, 10000);

    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)

                // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)

                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        //recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(CONTACT_FRAGMENT, menuGrammar);



    }

    @Override
    public void onError(Exception error) {

    }

    @Override
    public void onTimeout() {

    }
}

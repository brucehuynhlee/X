package a8.group.ttnm.x.view;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;


import a8.group.ttnm.x.controller.AutoCompleteContactAdapter;
import a8.group.ttnm.x.controller.PagerAdapter;
import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.RecognizeServiceManager.SpeechRecognizerManager;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFactory;


public class MainApp extends AppCompatActivity implements SpeechRecognizerManager.OnGoogleResultListener,SpeechRecognizerManager.OnPocketResultListener{

    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    AutoCompleteTextView autoContact ;
    AutoCompleteContactAdapter autoContactAdapter ;
    List<Contact> contacts ;
    ViewPager viewPager ;
    PagerAdapter pageAdapter ;
    ContactsFragment contactsFragment;
    CallHistoryFragment historyFragment ;

    // kernel recognize speech
    SpeechRecognizerManager mSpeechRecognize ;
    private static final String MENU = "menu";
    private static final String MENU_SEARCH = "search";
    private static final String MENU_HISTORY = "history";
    private static final String MENU_CONTACT = "contact";
    private static final String MENU_CHAT = "chat";
    private static final String MENU_FAVORITE = "favorite";

    private static final String CONTACT_OPTION = "contact option";
    private static final String CONTACT_OPTION_DELETE_CONFIRM = "delete confirm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        // init recognize speech virtual service
        mSpeechRecognize = new SpeechRecognizerManager(this,MENU);
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
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.ic_menu_call).setText("Danh bạ").setTag(MENU_CONTACT));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.stat_notify_chat).setText("Hộp thư thoại"));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.star_big_on).setText("Yêu thích"));
        //tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.ic_menu_gallery).setText("Nhóm"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
         pageAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);
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
        hideInputSoft();
        autoContact.setCursorVisible(false);
        Log.d("HUYNH","fragment"+(contactsFragment == null)+"");

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
        isInBackground = true ;
        if(mSpeechRecognize != null && mSpeechRecognize.mPocketSphinxRecognizer != null)
            mSpeechRecognize.mPocketSphinxRecognizer.cancel();
        super.onPause();
    }
    @Override
    public void onStop(){
        isInBackground = true ;
        if(mSpeechRecognize != null && mSpeechRecognize.mPocketSphinxRecognizer != null)
             mSpeechRecognize.mPocketSphinxRecognizer.cancel();
        super.onStop();
    }
    @Override
    public void onResume(){
        hideInputSoft();
        if(isInBackground){
            mSpeechRecognize.setPocketListening(MENU);
            Toast.makeText(this,"get back main app",Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    @Override
    public void onDestroy(){
        mSpeechRecognize.mPocketSphinxRecognizer.cancel();
        mSpeechRecognize.mPocketSphinxRecognizer.shutdown();
        super.onDestroy();
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

        }

    }

     // get result google recognize
    @Override
    public void OnGoogleResult(String result) {
        switch(mSpeechRecognize.mPocketSphinxRecognizer.getSearchName()){
            case MENU_SEARCH:
                autoContact.setText(result);
                mSpeechRecognize.setPocketListening(MENU_SEARCH);
            case MENU:
                mSpeechRecognize.setPocketListening(MENU);
        }
    }

    // get result pocket recognize
    @Override
    public void OnPocketResult(String result) {
        switch (mSpeechRecognize.mPocketSphinxRecognizer.getSearchName()){
            case MENU:
                switchMain(result);
                break;
            case MENU_SEARCH:
                switchSearch(result);
                break;
            case MENU_CONTACT:
                try{
                    switchContact(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case CONTACT_OPTION:
                try{
                    switchOptionContact(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case CONTACT_OPTION_DELETE_CONFIRM:
                switchConfirmDeleteContact(result);
                break;
        }


    }



    //menu level 1
    private void switchMain(String result){
        switch (result){
            case MENU_SEARCH:
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU_SEARCH);
                break;
            case MENU_HISTORY:
                viewPager.setCurrentItem(0);
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU_HISTORY);
                break;
            case MENU_CONTACT:
                viewPager.setCurrentItem(1);
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU_CONTACT);
                break;
            case MENU_CHAT:
                viewPager.setCurrentItem(2);
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU);
                break;
            case MENU_FAVORITE:
                viewPager.setCurrentItem(3);
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU);
                break;
            default:
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU);

        }
    }

    //menu level 2 search menu
    private void switchSearch(String result){

        switch (result){
            case "enter":
                autoContact.setCursorVisible(true);
                autoContact.setText("");
                mSpeechRecognize.setGoogleListening();
                break;
            case "clear":
                autoContact.setCursorVisible(true);
                autoContact.setText("");
                mSpeechRecognize.setPocketListening(MENU_SEARCH);
                break;
            case "ok":
                //break activity , not listening anymore
                Intent intent = new Intent(getApplicationContext(),DetailContactActivity.class);
                intent.putExtra("contactDetail",contacts.get(0));
                startActivity(intent);
                break;
            case "exit":
                autoContact.setCursorVisible(false);
                mSpeechRecognize.setPocketListening(MENU_CONTACT);
                break;
            default:
                mSpeechRecognize.mPocketSphinxRecognizer.startListening(MENU_SEARCH);
        }
    }

    // menu level 3 contact menu
    int contactPosition = 0 ;
    private void switchContact(String result){
        contactsFragment = (ContactsFragment) pageAdapter.getCurrentFragment() ;
        int sizeContact = contactsFragment.listDataContact.size();
        switch (result){
            case "choose":
                contactsFragment.collapseAll();
                contactsFragment.listContacts.smoothScrollToPosition(contactPosition);
                contactsFragment.listContacts.expandGroup(contactPosition);
                mSpeechRecognize.setPocketListening(CONTACT_OPTION);
                break;
            case "move down":
                contactsFragment.collapseAll();
                autoContact.setCursorVisible(false);
                autoContact.setText("");
                if(sizeContact == contactPosition) {
                    contactPosition = 0 ;
                }else contactPosition ++ ;
                contactsFragment.listContacts.smoothScrollToPosition(contactPosition);
                mSpeechRecognize.setPocketListening(MENU_CONTACT);
                break;
            case "move up":
                contactsFragment.collapseAll();
                autoContact.setCursorVisible(false);
                autoContact.setText("");
                if(contactPosition == 0) contactPosition = sizeContact-1 ;
                else contactPosition -- ;
                contactsFragment.listContacts.smoothScrollToPosition(contactPosition);
                mSpeechRecognize.setPocketListening(MENU_CONTACT);
                break;
            case "exit":
                contactsFragment.collapseAll();
                mSpeechRecognize.setPocketListening(MENU);
                break;
            default:
                mSpeechRecognize.setPocketListening(MENU_CONTACT);

        }
    }

    // menu level 4 Option contact
    private void switchOptionContact(String result){

        switch(result){
            case "call":
                contactsFragment.switchMenu(contactPosition,0);
                break;
            case "show":
                contactsFragment.switchMenu(contactPosition,1);
                break;
            case "edit":
                contactsFragment.switchMenu(contactPosition,2);
                break;
            case "delete":
                contactsFragment.collapseAll();
                Toast.makeText(this,"Nói yes nếu đồng ý , no nếu không đồng ý",Toast.LENGTH_SHORT).show();
                mSpeechRecognize.setPocketListening(CONTACT_OPTION_DELETE_CONFIRM);
                break;
            case "exit":
                contactsFragment.collapseAll();
                mSpeechRecognize.setPocketListening(MENU_CONTACT);
                break;
            default:
                mSpeechRecognize.setPocketListening(CONTACT_OPTION);
                break;
        }
    }

    // menu level 5 delete contact confirm menu
    private void switchConfirmDeleteContact(String result){
        switch(result) {
            case "yes":
                contactsFragment.listDataContact.remove(contactPosition);
                contactsFragment.contactsAdapter.notifyDataSetChanged();
                mSpeechRecognize.setPocketListening(MENU_CONTACT);
                break;
            case "no":
                contactsFragment.collapseAll();
                mSpeechRecognize.setPocketListening(MENU_CONTACT);
                break;
        }
    }

}

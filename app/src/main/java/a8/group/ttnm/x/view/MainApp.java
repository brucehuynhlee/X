package a8.group.ttnm.x.view;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import java.util.List;

import a8.group.ttnm.x.controller.AutoCompleteContactAdapter;
import a8.group.ttnm.x.controller.PagerAdapter;
import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.RecognizeSpeechService;
import a8.group.ttnm.x.controller.RecordPhoneCall.DeviceAdmin;
import a8.group.ttnm.x.controller.RecordPhoneCall.RecordService;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFactory;

public class MainApp extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    AutoCompleteTextView autoContact ;
    AutoCompleteContactAdapter autoContactAdapter ;
    List<Contact> contacts ;
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

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
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
}

package a8.group.ttnm.x.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import a8.group.ttnm.x.controller.PagerAdapter;
import a8.group.ttnm.x.R;

public class MainApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        /**
         * fix icon later
         */
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

package a8.group.ttnm.x.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import a8.group.ttnm.x.view.CallHistoryFragment;
import a8.group.ttnm.x.view.ContactsFragment;
import a8.group.ttnm.x.view.GroupFragment;
import a8.group.ttnm.x.view.VoiceMailFragment;

/**
 * Created by LittleDragon on 11/6/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CallHistoryFragment tab1 = new CallHistoryFragment();
                return tab1;
            case 1:
                ContactsFragment tab2 = new ContactsFragment();
                return tab2;
            case 2:
                VoiceMailFragment tab3 = new VoiceMailFragment();
                return tab3;
            case 3:
                GroupFragment tab4 = new GroupFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}

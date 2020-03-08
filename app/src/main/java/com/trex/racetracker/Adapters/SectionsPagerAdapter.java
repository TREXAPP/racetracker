package com.trex.racetracker.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import layout.EntriesTab;
import layout.InputTab;
import layout.LoginTab;
import layout.RacersTab;

/**
 * Created by Igor on 22.10.2016.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        Fragment returnFragment = new Fragment();
        switch (position) {
            case 0:
                returnFragment = InputTab.newInstance(position+1);
                break;
            case 1:
                returnFragment = EntriesTab.newInstance(position+1);
                break;
            case 2:
                returnFragment = RacersTab.newInstance(position+1);
                break;
            case 3:
                returnFragment = LoginTab.newInstance(position+1);
                break;
        }

        return returnFragment;

     //     return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "INPUT";
            case 1:
                return "ENTRIES";
            case 2:
                return "RACERS";
            case 3:
                return "LOGIN";
        }
        return null;
    }
}

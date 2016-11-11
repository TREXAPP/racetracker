package com.trex.racetracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import layout.InputR;

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
                returnFragment = InputR.newInstance(position+1);
                break;
            case 1:
                returnFragment = layout.Entries.newInstance(position+1);
                break;
            case 2:
                returnFragment = layout.Racers.newInstance(position+1);
                break;
            case 3:
                returnFragment = layout.Sync.newInstance(position+1);
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
                return "SYNC";
        }
        return null;
    }
}

package com.trex.racetracker;
import android.content.Context;
import android.provider.Settings.Secure;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import layout.Input;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static final String GLOBALS = "GLOBALS";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

         myDb = new DatabaseHelper(this);
        InitializeGlobals(myDb);

    }

    private void InitializeGlobals(DatabaseHelper myDb) {

        //TO DO - ability to change these in settings

        SharedPreferences globals = getSharedPreferences(GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();

        if (!globals.contains("islogin")) editor.putString("islogin","0");
        if (!globals.contains("username")) editor.putString("username","");
        if (!globals.contains("starttime")) editor.putString("starttime","");
        if (!globals.contains("operator")) editor.putString("operator","");
        if (!globals.contains("controlpoint")) editor.putString("controlpoint","");
        if (!globals.contains("inputdigitsno")) editor.putInt("inputdigitsno",3);
        if (!globals.contains("entryconfirmtimer")) editor.putInt("entryconfirmtimer",100);
        String android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
       // if (!globals.contains("deviceid"))
            editor.putString("deviceid",android_id);


        editor.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

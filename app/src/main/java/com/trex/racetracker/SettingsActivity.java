package com.trex.racetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;


/**
 * Created by Igor on 04.6.2017.
 */

public class SettingsActivity extends AppCompatPreferenceActivity {
    private final static String TAG = "SettingsActivity";
    public Context context;
    public SettingsActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setupActionBar();
        Log.d(TAG, "onCreate");
        String Fragment = getIntent().getStringExtra("Fragment");
        switch(Fragment) {
            case "SyncFragment":
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new SynchronizationFragment()).commit();
                break;
            case "AdminFragment":
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new AdminFragment()).commit();
                break;
            case "AboutFragment":
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new AboutFragment()).commit();
                break;

        }

    }
    public class SynchronizationFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final static String TAG = "SynchronizationFragment";

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            InitializePrefs(TAG);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);
            SharedPreferences.Editor editor = globals.edit();
            switch (key) {
                case "pref_periodic_sync":
                    editor.putBoolean("periodic_sync",sharedPreferences.getBoolean(key,true));
                    if (!sharedPreferences.getBoolean("pref_periodic_sync",true)) {
                        getPreferenceScreen().findPreference("pref_sync_interval").setEnabled(false);
                    } else {
                        getPreferenceScreen().findPreference("pref_sync_interval").setEnabled(true);
                    }
                    break;
                case "pref_sync_interval":
                    editor.putLong("sync_interval",Long.parseLong(sharedPreferences.getString(key,"60")));
                    break;

            }
            editor.apply();

        }



        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            try {
                getActionBar().setTitle("Synchronization Settings");
            } catch (NullPointerException e) {
                Log.e("setTitle","NullPointerException");
            }
            getSupportActionBar().setTitle("Synchronization Settings");

            Log.d(TAG, "onCreate");
            addPreferencesFromResource(R.xml.pref_sync);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (!prefs.getBoolean("pref_periodic_sync",true)) {
                getPreferenceScreen().findPreference("pref_sync_interval").setEnabled(false);
            } else {
                getPreferenceScreen().findPreference("pref_sync_interval").setEnabled(true);
            }
        }


    }

    private void InitializePrefs(String tag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);

        switch (tag) {
            case SynchronizationFragment.TAG:
                editor.putBoolean("pref_periodic_sync",globals.getBoolean("periodic_sync",true));
                editor.putString("pref_sync_interval",Long.toString(globals.getLong("sync_interval",60)));
                break;

            case AdminFragment.TAG:
                editor.putBoolean("pref_allowemptyentries",globals.getBoolean("allowemptyentries",true));
                editor.putString("pref_inputdigitsno",String.valueOf(globals.getInt("inputdigitsno",3)));
                editor.putString("pref_timebetweenentries",String.valueOf(globals.getInt("timebetweenentries",10)));
                editor.putString("pref_entryvisualconfirmtimer",String.valueOf(globals.getInt("entryvisualconfirmtimer",400)));

                break;

            case AboutFragment.TAG:
                break;
        }
        editor.commit();
    }

    public class AdminFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final static String TAG = "AdminFragment";


        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            InitializePrefs(TAG);
            try {
                getActionBar().setTitle("Administrator Settings");
            } catch (NullPointerException e) {
                Log.e("setTitle","NullPointerException");
            }
            getSupportActionBar().setTitle("Administrator Settings");
            Log.d(TAG, "onCreate");
            addPreferencesFromResource(R.xml.pref_admin);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (!IsAdminPasswordOK(prefs.getString("pref_admin_lock",""))) {
                getPreferenceScreen().findPreference("pref_allowemptyentries").setEnabled(false);
                getPreferenceScreen().findPreference("pref_inputdigitsno").setEnabled(false);
                getPreferenceScreen().findPreference("pref_timebetweenentries").setEnabled(false);
                getPreferenceScreen().findPreference("pref_entryvisualconfirmtimer").setEnabled(false);
            } else {
                getPreferenceScreen().findPreference("pref_allowemptyentries").setEnabled(true);
                getPreferenceScreen().findPreference("pref_inputdigitsno").setEnabled(true);
                getPreferenceScreen().findPreference("pref_timebetweenentries").setEnabled(true);
                getPreferenceScreen().findPreference("pref_entryvisualconfirmtimer").setEnabled(true);
            }

        }

        private boolean IsAdminPasswordOK(String pref_admin_lock) {
            //get hardcoded admin password
            SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);
            String hardcodedPass = globals.getString("AdminPass","");
            if (hardcodedPass.equals(pref_admin_lock)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);
            SharedPreferences.Editor editor = globals.edit();
            switch (key) {
                case "pref_admin_lock":
                    if (!IsAdminPasswordOK(sharedPreferences.getString("pref_admin_lock",""))) {
                        getPreferenceScreen().findPreference("pref_allowemptyentries").setEnabled(false);
                        getPreferenceScreen().findPreference("pref_inputdigitsno").setEnabled(false);
                        getPreferenceScreen().findPreference("pref_timebetweenentries").setEnabled(false);
                        getPreferenceScreen().findPreference("pref_entryvisualconfirmtimer").setEnabled(false);
                    } else {
                        getPreferenceScreen().findPreference("pref_allowemptyentries").setEnabled(true);
                        getPreferenceScreen().findPreference("pref_inputdigitsno").setEnabled(true);
                        getPreferenceScreen().findPreference("pref_timebetweenentries").setEnabled(true);
                        getPreferenceScreen().findPreference("pref_entryvisualconfirmtimer").setEnabled(true);
                    }
                    break;
                case "pref_allowemptyentries":
                    editor.putBoolean("allowemptyentries",sharedPreferences.getBoolean(key,true));
                    break;

                case "pref_inputdigitsno":
                    editor.putInt("inputdigitsno",Integer.parseInt(sharedPreferences.getString(key,"3")));
                    break;

                case "pref_timebetweenentries":
                    editor.putInt("timebetweenentries",Integer.parseInt(sharedPreferences.getString(key,"10")));
                    break;

                case "pref_entryvisualconfirmtimer":
                    editor.putInt("entryvisualconfirmtimer",Integer.parseInt(sharedPreferences.getString(key,"400")));
                    break;

            }
            editor.apply();
        }
    }



    public class AboutFragment extends PreferenceFragment {

        private final static String TAG = "AboutFragment";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            try {
                getActionBar().setTitle("About");
            } catch (NullPointerException e) {
                Log.e("setTitle","NullPointerException");
            }
            getSupportActionBar().setTitle("About");

            Log.d(TAG, "onCreate");
            addPreferencesFromResource(R.xml.pref_about);

            SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            String Summary = "";
            Summary += "\n";
            Summary += "DeviceID:   ";
            Summary += globals.getString("deviceid","N/A") + "\n";
            Summary += "============================\n";
            Summary += "App version:   ";
            Summary += globals.getString("version","N/A");
            Summary += "\n\n";

            Summary += "Copyright TREX™ \n\n";

            Summary += "Author:   ";
            Summary += "Igor Josifov\n";
            Summary += "+389 78 833 228  |  ";
            Summary += "igority@gmail.com\n";
            Summary += "============================\n";
            Summary += "Disclaimer: This app, along with the web tracking system was created solely for the purpose of the CAT™ tracking system, to be used for, but not exclusivelly, the Krali Marko Trails 2017 race\n";
            Summary += "The Author holds the full rights of the app and the tracking system, and can be used only with his consent.\n";
            Summary += "Any unauthorised using, copying, muliplying and modifying is strictly FORBIDDEN and on such actions will be responded legaly.\n";
            Summary += "\n\n";


            getPreferenceScreen().findPreference("pref_about_key").setSummary(Summary);

        }
    }

    private void setupActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle "up" button behavior here.
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            // handle other items here
            return false;
        }
    }


}

package com.trex.racetracker;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import layout.Input;

import static android.service.notification.Condition.SCHEME;
import static com.trex.racetracker.StaticMethods.*;
import static java.security.AccessController.getContext;

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
    private Observer mObserver = null;
    private Uri mUri = null;

    DatabaseHelper myDb;

    //***********Sync******************
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.trex.racetracker.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.trex.racetracker.datasync";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;
    // A content resolver for accessing the provider
    ContentResolver mResolver;

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


         myDb = DatabaseHelper.getInstance(this);
        InitializeGlobals(myDb);

        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        // Construct a URI that points to the content provider data table
        //mUri = Uri.withAppendedPath(Provider.CONTENT_URI,"CPEntries");
        mUri = Provider.CONTENT_URI;

        //mUri.parse(mProvider.SCHEME + mProvider.AUTHORITY);

        // creates and starts a new thread set up as a looper
       // HandlerThread thread = new HandlerThread("MyHandlerThread");
        //thread.start();

// creates the handler using the passed looper
       // Handler handler = new Handler(thread.getLooper());

        /*
         * Create a content observer object.
         * Its code does not mutate the provider, so set
         * selfChange to "false"
         */
        mObserver = new Observer(new Handler(),this,mAccount);
        /*
         * Register the observer for the data table. The table's path
         * and any of its subpaths trigger the observer.
         */
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(mUri, true, mObserver);
        final SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);
        ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        TogglePeriodicSync(mAccount, this);

    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */

            //check if account exists
            Account[] accounts = accountManager.getAccounts();
            for (Account account : accounts) {
                if (account.name.equals(newAccount.name)) {
                    return account;
                }
            }
            Log.e("error","Error creating account");
            return null;
        }
    }

    private void InitializeGlobals(DatabaseHelper myDb) {

        //TO DO - ability to change these in settings

        SharedPreferences globals = getSharedPreferences(GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();

        //when a user gets logged in this is set true. When no user is logged in it is false
        if (!globals.contains("islogin")) editor.putBoolean("islogin",false);

        //the username for the logged user. if no user is logged, this is empty string
        if (!globals.contains("username")) editor.putString("username","");

        //the starting time of the race. So far it has no uses (TODO?)
        if (!globals.contains("starttime")) editor.putString("starttime","");

        //the operator that is set when user is logged. Can be empty string even if user is logged. If no user is logged it is also an empty string
        if (!globals.contains("operator")) editor.putString("operator","");

        //The name of the control point that corresponds to the user logged. Empty string if no user is logged.
        if (!globals.contains("controlpoint")) editor.putString("controlpoint","");

        //The default number of digits that the BIB numbers have. Default is 3, but can be changed in parameters. Important for the Input
        if (!globals.contains("inputdigitsno")) editor.putInt("inputdigitsno",3);

        //The time (in miliseconds) that the entered BIB number stays flashing before it dissapears. Default is 100ms, but can be changed in parameters
        if (!globals.contains("entryconfirmtimer")) editor.putInt("entryconfirmtimer",400);

        //whether or not to allow entry of nonexisting racers. If you are sure that all the racers are correctly syncronized and
        //if there wont be last minute changes that is possible not to be syncronized, then you can set this false. Otherwise,
        //it is advisable for this to be left true
        if (!globals.contains("allowemptyentries")) editor.putBoolean("allowemptyentries",true);

        //the unique ID of the device. Used to destinguish the entries
        String android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
        if (!globals.contains("deviceid")) editor.putString("deviceid",android_id);

        //time (in minutes) after which all entries from one control points are ignored. They are stored for reference with flag valid=false
        if (!globals.contains("timebetweenentries")) editor.putInt("timebetweenentries",1);

        //whether or not periodic sync for the entries is enabled. Will be a parameter in the settings to toggle
        //when ON it ensures successful sync, but may drain battery more
        if (!globals.contains("periodic_sync")) editor.putBoolean("periodic_sync",true);

        //TIme interval (in seconds) between two attempts at periodic sync (only if globals("periodic_sync")=true)
        //larger value - saves battery more, but increases delay for syncing
        if (!globals.contains("sync_interval")) editor.putLong("sync_interval",60L);

        editor.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ContentResolver contentResolver = getContentResolver();
        contentResolver.unregisterContentObserver(mObserver);
        TogglePeriodicSync(mAccount,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(mUri, true, mObserver);
        TogglePeriodicSync(mAccount,this);
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

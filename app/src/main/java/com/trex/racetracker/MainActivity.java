package com.trex.racetracker;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import layout.Input;

import static android.service.notification.Condition.SCHEME;
import static com.trex.racetracker.DbMethods.*;
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

    NfcAdapter nfcAdapter;
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

    private BroadcastReceiver mReceiver;
    private TextView tvSyncInfo;
   // private ImageView ivSyncToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //ivSyncToolbar = (ImageView) findViewById(R.id.ivSyncToolbar);
        tvSyncInfo = (TextView) findViewById(R.id.tvSyncInfo);

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

        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("com.trex.racetracker.UPDATE_LAST_SYNC");
        myFilter.addAction("com.trex.racetracker.REFRESH_LIST_INPUT");
        myFilter.addAction("com.trex.racetracker.REFRESH_LIST_ENTRIES");
        if (mReceiver == null) mReceiver = InitializeBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,myFilter);

        //initialize info for sync
       // ivSyncToolbar.setVisibility(View.INVISIBLE);
        tvSyncInfo.setText("Last sync: Never");


        //initialize search and filter for Entries
       // SharedPreferences globals = getSharedPreferences(GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        editor.putString("EntriesSelectionSearch","1");
        editor.putString("EntriesSelectionFilter","1");
        editor.putBoolean("EntriesSelectionLeftJoin",true);
        editor.commit();

    }

    private BroadcastReceiver InitializeBroadcastReceiver() {

        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String sAction = intent.getAction();
                if ("com.trex.racetracker.UPDATE_LAST_SYNC".equals(sAction) )
                {
                    SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);
                    Long lastSync = globals.getLong("lastPushInMillis",0);

                    if (lastSync == 0) {
                        tvSyncInfo.setText("Last sync: Never");
                    } else {
                        String dateString = new SimpleDateFormat("HH:mm:ss").format(new Date(lastSync));
                        tvSyncInfo.setText("Last sync: " + dateString);
                    }
                  //  ivSyncToolbar.setVisibility(View.INVISIBLE);

                }

            }
        };
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

        //App version
        //if (!globals.contains("version"))
            editor.putString("version","1.103");

        //App version
       // if (!globals.contains("AdminPass"))
            editor.putString("AdminPass","run358");

        //when a user gets logged in this is set true. When no user is logged in it is false
        if (!globals.contains("islogin")) editor.putBoolean("islogin",false);

        //the username for the logged user. if no user is logged, this is empty string
        if (!globals.contains("username")) editor.putString("username","");

        //the operator that is set when user is logged. Can be empty string even if user is logged. If no user is logged it is also an empty string
        if (!globals.contains("operator")) editor.putString("operator","");

        //The name of the control point that corresponds to the user logged. Empty string if no user is logged.
        if (!globals.contains("controlpoint")) editor.putString("controlpoint","");

        //The default number of digits that the BIB numbers have. Default is 3, but can be changed in parameters. Important for the Input
        if (!globals.contains("inputdigitsno")) editor.putInt("inputdigitsno",3);

        //The time (in miliseconds) after which the entered digits are reseted. This is to avoid unintentional entries.
        if (!globals.contains("entryresettimer")) editor.putInt("entryresettimer",5000);

        //The time (in miliseconds) that the entered BIB number stays flashing before it dissapears. Default is 100ms, but can be changed in parameters
        if (!globals.contains("entryvisualconfirmtimer")) editor.putInt("entryvisualconfirmtimer",400);

        //whether or not to allow entry of nonexisting racers. If you are sure that all the racers are correctly syncronized and
        //if there wont be last minute changes that is possible not to be syncronized, then you can set this false. Otherwise,
        //it is advisable for this to be left true
        if (!globals.contains("allowemptyentries")) editor.putBoolean("allowemptyentries",true);

        //the unique ID of the device. Used to destinguish the entries
        String android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
        if (!globals.contains("deviceid")) editor.putString("deviceid",android_id);

        //time (in minutes) after which all entries from one control points are ignored. They are stored for reference with flag valid=false
        if (!globals.contains("timebetweenentries")) editor.putInt("timebetweenentries",10);

        //whether or not periodic sync for the entries is enabled. Will be a parameter in the settings to toggle
        //when ON it ensures successful sync, but may drain battery more
        if (!globals.contains("periodic_sync")) editor.putBoolean("periodic_sync",true);

        //TIme interval (in seconds) between two attempts at periodic sync (only if globals("periodic_sync")=true)
        //larger value - saves battery more, but increases delay for syncing
        if (!globals.contains("sync_interval")) editor.putLong("sync_interval",60L);

        //when was the last push of entries made. 0 = never
        if (!globals.contains("lastPushInMillis")) editor.putLong("lastPushInMillis",0);

        //when was the last pull of entries made. 0 = never
        //if (!globals.contains("lastPullInMillis")) editor.putLong("lastPullInMillis",0);

        editor.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ContentResolver contentResolver = getContentResolver();
        contentResolver.unregisterContentObserver(mObserver);
        TogglePeriodicSync(mAccount,this);
        //nfc
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // adapter exists and is enabled.
            disableForegroundDispatchSystem();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(mUri, true, mObserver);
        TogglePeriodicSync(mAccount,this);
        //nfc
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // adapter exists and is enabled.
            enableForegroundDispatchSystem();
        }

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



        return super.onOptionsItemSelected(item);
    }

    public void ToggleSync(MenuItem item) {
        SharedPreferences globals = getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        if (item.isChecked()) {
            item.setChecked(false);
            editor.putBoolean("periodic_sync",false);
            Toast.makeText(this, "Synchronization disabled", Toast.LENGTH_SHORT).show();
        } else {
            item.setChecked(true);
            editor.putBoolean("periodic_sync",true);
            Toast.makeText(this, "Synchronization enabled", Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }

    public void OpenSyncSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Fragment","SyncFragment");
        startActivity(intent);

    }

    public void OpenAboutSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Fragment","AboutFragment");
        startActivity(intent);
    }

    public void OpenAdminSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Fragment","AdminFragment");
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Toast.makeText(this, "NFC intent received!", Toast.LENGTH_SHORT).show();
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            //Toast.makeText(this,"NfcIntent!", Toast.LENGTH_SHORT).show();

          //  if (tglReadWrite.isChecked()) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (parcelables != null && parcelables.length > 0) {
                    readTextFromTag((NdefMessage)parcelables[0]);
                } else {
                    Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
                }
         //   } else {
                //code for writing nfc. We don't need this

                /*
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage(etTagContent.getText()+ "");

                writeNdefMessage(tag, ndefMessage);
                */
            }

        }




    private void readTextFromTag(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);

            SharedPreferences globals = getSharedPreferences(GLOBALS,0);

            final ViewGroup layoutInput = (ViewGroup) findViewById(R.id.layoutInput);
            final TextView tvBIBEntry = (TextView) findViewById(R.id.tvBIBEntry);
            final ListView lvInputEntries = (ListView) findViewById(R.id.lvInputEntries);
            //TODO logic here!
            //1. form an entry
            //2. write the entry in sqlite
            //3. request sync
            //4. Refresh listviews (if needed)

            EntryObj entryObj = PrepareEntryObj(this, tagContent,3,null);
            String status = ValidateEntry(this,entryObj);

            disableEnableControls(false,layoutInput);
            Integer timer = globals.getInt("entryvisualconfirmtimer",400);




            new CountDownTimer(timer, timer) {

                public void onTick(long millisUntilFinished) {
                    //nothing to do here
                }

                public void onFinish() {

                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FF7BFDB1"));
                    tvBIBEntry.setTextColor(Color.parseColor("#FF000000"));
                    tvBIBEntry.setText("");
                    layoutInput.setBackgroundColor(Color.TRANSPARENT); //TRANSPARENT
                    disableEnableControls(true,layoutInput);
                }
            }.start();


            MediaPlayer mpSuccess = MediaPlayer.create(this, R.raw.beep_short);
            MediaPlayer mpError = MediaPlayer.create(this, R.raw.beep_long);


            tvBIBEntry.setText(tagContent);
            //set colors depending on status:
            switch (status) {
                case "not logged in":
                    //  tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
                    //  tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));
                    layoutInput.setBackgroundColor(Color.parseColor("#FFBBD0BF"));  //light gray
                    mpSuccess.start();
                    break;
                case "success":
                    //  tvBIBEntry.setBackgroundColor(Color.parseColor("#FFD9DDFF"));
                    // tvBIBEntry.setTextColor(Color.parseColor("#FF004F0D"));
                    layoutInput.setBackgroundColor(Color.parseColor("#FF7BFDB1"));  //lightest green
                    mpSuccess.start();
                    break;
                case "too soon":
                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));   //red
                    tvBIBEntry.setTextColor(Color.parseColor("#FFFFFFFF"));         //white
                    layoutInput.setBackgroundColor(Color.parseColor("#FFFF0004"));  //red
                    mpError.start();
                    break;
                case "already passed":
                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FF3E50FF"));   //blue
                    tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));         //lightest green
                    layoutInput.setBackgroundColor(Color.parseColor("#FF3E50FF"));  //blue
                    mpError.start();
                    break;
                default:
                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
                    tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));
                    mpSuccess.start();
                    break;
            }

            if (!(insertIntoCPEntries(this,entryObj,true))) {
                Toast.makeText(this, "Error writing Entry into local database! Contact the administrator.", Toast.LENGTH_SHORT).show();
            } else {
                PopulateInputEntriesListView(this,lvInputEntries, MainActivity.this);
            }

            SharedPreferences.Editor editor = globals.edit();
            editor.putString("EntryNoState","");
            editor.commit();

            tvBIBEntry.setText("");

        } else {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableForegroundDispatchSystem () {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);

    }

    private void disableForegroundDispatchSystem () {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_LONG).show();
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag formated!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("formatTag",e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {

            if(tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                //format tag with the ndef format and writes the message
                formatTag(tag,ndefMessage);

            }
            else {
                ndef.connect();

                if(!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }
    }

    private NdefRecord createTextRecord (String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text,0,textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ ndefRecord });
        return ndefMessage;
    }

    public String getTextFromNdefRecord (NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }
}


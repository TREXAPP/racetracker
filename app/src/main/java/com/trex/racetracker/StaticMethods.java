package com.trex.racetracker;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import layout.Entries;

import static com.trex.racetracker.DbMethods.*;
import static com.trex.racetracker.MainActivity.AUTHORITY;

/**
 * Created by Igor_2 on 03.11.2016.
 */

public class StaticMethods {

    public StaticMethods() {
        //default constructor
    }


    public static void InitializeLoginFragment(Context context, View fragmentLogin, SharedPreferences globals) {

        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        EditText etUsername = (EditText) fragmentLogin.findViewById(R.id.etUsername);
        EditText etPassword = (EditText) fragmentLogin.findViewById(R.id.etPassword);
        EditText etOperator = (EditText) fragmentLogin.findViewById(R.id.etOperator);
        Button btnLogin = (Button) fragmentLogin.findViewById(R.id.btnLogin);
        Button btnLogout = (Button) fragmentLogin.findViewById(R.id.btnLogout);
        TextView tvUsername = (TextView) fragmentLogin.findViewById(R.id.tvUsername);
        TextView tvOperator = (TextView) fragmentLogin.findViewById(R.id.tvOperator);
        TextView tvControlPoint = (TextView) fragmentLogin.findViewById(R.id.tvControlPoint);
        ListView lvRacesLogin = (ListView) fragmentLogin.findViewById(R.id.lvRacesLogin);
        LinearLayout layoutLoginInfo = (LinearLayout) fragmentLogin.findViewById(R.id.layoutLoginInfo);

        Boolean loggedIn = false;
        globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        String Username = globals.getString("username","");
        String Operator = globals.getString("operator","");
        String controlPoint = globals.getString("controlpoint","");
        if (globals.getBoolean("islogin",false)) loggedIn = true;
        if (loggedIn) {
            //user is logged in. Show info and form for logout
            tvUsername.setText(Username);
            tvOperator.setText(Operator);
            tvControlPoint.setText(controlPoint);
            String displayStatusTop = "Logged in";
            tvStatusTop.setText(displayStatusTop);
            etUsername.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            etOperator.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);

            //populate listview with races
            PopulateRacesListView(context,lvRacesLogin);


            btnLogout.setVisibility(View.VISIBLE);
            layoutLoginInfo.setVisibility(View.VISIBLE);
            lvRacesLogin.setVisibility(View.VISIBLE);
            tvStatusTop.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

        } else {
            //user is not logged, show for for logging in
            String displayStatusTop = "Not logged in";
            tvStatusTop.setText(displayStatusTop);
            tvStatusTop.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etOperator.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
            layoutLoginInfo.setVisibility(View.GONE);
            lvRacesLogin.setVisibility(View.GONE);
        }
    }

    public static void InitializeInputFragment(Context context, View fragmentInput, Activity activity) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        TextView tvBIBEntry = (TextView) fragmentInput.findViewById(R.id.tvBIBEntry);
        ListView lvInputEntries = (ListView) fragmentInput.findViewById(R.id.lvInputEntries);
        if (!globals.contains("EntryNoState")) {
            SharedPreferences.Editor editor = globals.edit();
            editor.putString("EntryNoState","");
            editor.commit();
        }
        tvBIBEntry.setText(globals.getString("EntryNoState",""));
        PopulateInputEntriesListView(context,lvInputEntries,activity);
    }

    public static void PopulateInputEntriesListView (Context context, ListView lvInputEntries, Activity activity) {
        //DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        Cursor cursorInputEntries = getEntriesInput(context,true,true,10,"","Time DESC");

        EntryObj[] EntryObjArray = new EntryObj[cursorInputEntries.getCount()];
        int i=0;
        cursorInputEntries.moveToFirst();
        while (!cursorInputEntries.isAfterLast()) {

            String BIB = cursorInputEntries.getString(0);
            String Time =  cursorInputEntries.getString(1);
            Long TimeStamp =  cursorInputEntries.getLong(2);
            String Synced = cursorInputEntries.getString(3);
            String Name = null;
            String LastName = null;
            String Country = null;
            int Age = 0;
            String Gender = null;


            Cursor cursorRacer = getActiveRacerIDFromRacers(context, BIB);
            if (cursorRacer.getCount() > 0) {
                cursorRacer.moveToFirst();
                Name = cursorRacer.getString(1);
                LastName = cursorRacer.getString(2);
                Country = cursorRacer.getString(3);
                Age = calculateAge(String.valueOf(cursorRacer.getString(5)));
                Gender = cursorRacer.getString(4);
            }
            cursorRacer.close();

            EntryObjArray[i] = new EntryObj();

            if (BIB != null) EntryObjArray[i].setBIB(BIB);
            else EntryObjArray[i].setBIB("");

            if (Time != null) EntryObjArray[i].setTime(Time);
            else EntryObjArray[i].setTime("");

            if (TimeStamp != null) EntryObjArray[i].setTimeStamp(TimeStamp);
            else EntryObjArray[i].setTimeStamp((long) 0);

            if (Name != null) EntryObjArray[i].setFirstName(Name);
            else EntryObjArray[i].setFirstName("");

            if (LastName != null) EntryObjArray[i].setLastName(LastName);
            else EntryObjArray[i].setLastName("");

            if (Country != null) EntryObjArray[i].setCountry(Country);
            else EntryObjArray[i].setCountry("");

            if (Age != 0) EntryObjArray[i].setAge(Age);
            else EntryObjArray[i].setAge(0);

            if (Gender != null) EntryObjArray[i].setGender(Gender);
            else EntryObjArray[i].setGender("");


            if (Synced != null) {
                if (Synced.equals("1")) EntryObjArray[i].setSynced(true);
                else EntryObjArray[i].setSynced(false);
            }
            else EntryObjArray[i].setSynced(false);

            i++;
            cursorInputEntries.moveToNext();
        }
        cursorInputEntries.close();

        ListAdapter entriesInputAdapter = new EntriesInputListAdapter(context, EntryObjArray, activity, lvInputEntries);
        lvInputEntries.setAdapter(entriesInputAdapter);

    }

    public static void PopulateRacesListView(Context context, ListView lvRacesLogin) {

        //DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        Cursor cursorRaces = getDistinctRacesFromLoginInfo(context);
        final SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        RaceObj[] RacesObjArray = new RaceObj[cursorRaces.getCount()];
        int i=0;
        cursorRaces.moveToFirst();
        while(!cursorRaces.isAfterLast()) {

            final String RaceID = cursorRaces.getString(0);
            String RaceDescription = cursorRaces.getString(1);
            String CPNo = "";
            Cursor cursorCPNo = getDistinctCPNoFromLoginInfo(context,"RaceID=" + RaceID);
            if (cursorCPNo != null && cursorCPNo.getCount()>0) {
                cursorCPNo.moveToFirst();
                while (!cursorCPNo.isAfterLast()) {
                    if (!CPNo.equals("")) {
                        CPNo += ", ";
                    }
                   CPNo += cursorCPNo.getString(0);
                    cursorCPNo.moveToNext();
                }

            }
            cursorCPNo.close();

/*
            if (globals.contains("showRacers" + RaceID)) {
                if (globals.getString("showRacers" + RaceID,"0").equals("0")) {
                    ShowRacers = false;
                } else {
                    ShowRacers = true;
                }
            } else {
                // set for testing, this Toast should never happen!
                String toastText = "Warning! Global variable showRacers" + cursorRaces.getString(0) + "is not set properly! Contact the administrator!";
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            }
*/
            RacesObjArray[i] = new RaceObj();
            RacesObjArray[i].setRaceID(RaceID);
            RacesObjArray[i].setRaceDescription(RaceDescription);
            RacesObjArray[i].setCPNo(CPNo);

            i++;
            cursorRaces.moveToNext();



          }
        cursorRaces.close();

        ListAdapter racesAdapter = new RacesAdapter(context, RacesObjArray);
        lvRacesLogin.setAdapter(racesAdapter);



    }

    //Populate the listview in the Racers fragment. now with expandablelistview!
    public static void InitializeRacersFragment(Context context, View viewRacers, final SharedPreferences globals) {

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        final Cursor cursorDistinctRaces = getDistinctRacesFromLoginInfo(context);

        List<String> listDataHeader;
        HashMap<String, List<ActiveRacerObj>> listDataChild;
        ActiveRacersExpandableAdapter listAdapter;
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ActiveRacerObj>>();

        int i=0;
        cursorDistinctRaces.moveToFirst();
        while (!cursorDistinctRaces.isAfterLast()) {

            listDataHeader.add(cursorDistinctRaces.getString(1));
            Cursor cursorRacers = getActiveRacersForListView(context,"RaceID=" + cursorDistinctRaces.getString(0));

            List<ActiveRacerObj> childList = new ArrayList<ActiveRacerObj>();
            int j=0;
            cursorRacers.moveToFirst();
            while (!cursorRacers.isAfterLast()) {

                ActiveRacerObj child = new ActiveRacerObj();

                if (cursorRacers.getString(0) != null && !cursorRacers.getString(0).equals("null")) {
                    child.setBIB(cursorRacers.getString(0));
                } else  child.setBIB("");
                if (cursorRacers.getString(1) != null && !cursorRacers.getString(1).equals("null")) {
                    child.setFirstName(cursorRacers.getString(1));
                } else  child.setFirstName("");
                if (cursorRacers.getString(2) != null && !cursorRacers.getString(2).equals("null")) {
                    child.setLastName(cursorRacers.getString(2));
                } else  child.setLastName("");
                if (cursorRacers.getString(3) != null && !cursorRacers.getString(3).equals("null")) {
                    child.setCountry(cursorRacers.getString(3));
                } else  child.setCountry("");
                if (cursorRacers.getString(4) != null && !cursorRacers.getString(4).equals("null")) {
                    child.setDateOfBirth(cursorRacers.getString(4));
                } else  child.setDateOfBirth("");
                if (cursorRacers.getString(5) != null && !cursorRacers.getString(5).equals("null")) {
                    child.setGender(cursorRacers.getString(5));
                } else  child.setGender("");

                Cursor lastEntryRow = getLastEntryRow(context, cursorRacers.getString(6));
                lastEntryRow.moveToFirst();
                /*
                if (lastEntryRow.getCount()>0) {
                    if (lastEntryRow.getString(0) != null && !lastEntryRow.getString(0).equals("null")) {
                        child.setTimeLast(lastEntryRow.getString(0).substring(11,19));
                    } else {
                        child.setTimeLast("");
                    }

                    String CPNoString = "";
                    if (lastEntryRow.getString(1) != null && !lastEntryRow.getString(1).equals("null")) {
                        CPNoString += "CP " + lastEntryRow.getString(1);
                    }
                    child.setCPNo(CPNoString);

                    if (lastEntryRow.getString(2) != null && !lastEntryRow.getString(2).equals("null")) {
                        child.setCPName(lastEntryRow.getString(2));
                    }
                    lastEntryRow.close();

                } else {
                    child.setTimeLast("");
                    child.setCPNo("");
                    child.setCPName("");
                }
                */
                lastEntryRow.close();
                childList.add(child);
                j++;
                cursorRacers.moveToNext();
            }
            cursorRacers.close();
            listDataChild.put(listDataHeader.get(i),childList);
            i++;
            cursorDistinctRaces.moveToNext();
        }
        cursorDistinctRaces.close();

        listAdapter = new ActiveRacersExpandableAdapter(context, listDataHeader, listDataChild);
        final ExpandableListView expListView = (ExpandableListView) viewRacers.findViewById(R.id.elvRacers);
        expListView.setAdapter(listAdapter);

        //if only one race crosses this control point set specific default state for Racers expandableListView
        if (cursorDistinctRaces.getCount() == 1 ) {
            expListView.expandGroup(0);
        }



        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                SharedPreferences.Editor editor = globals.edit();
                if (cursorDistinctRaces.getCount() == 1) {
                 //   editor.putString("elvRacersState" + groupPosition,"1");
                    expListView.expandGroup(0);
                } else {
                    editor.putString("elvRacersState" + groupPosition,"0");
                }

                editor.apply();

            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                SharedPreferences.Editor editor = globals.edit();
                editor.putString("elvRacersState" + groupPosition,"1");
                editor.apply();
            }
        });


    }

    public static String FormatErrorString(String str) {
        String myStr = str;
        if (myStr != null && myStr.length() > 0 && myStr.charAt(myStr.length()-1)==';') {
            myStr = myStr.substring(0, myStr.length()-1);
        }
        if (myStr != null && myStr.length() > 0) {
            myStr = myStr.replace(';','\n');
        }
        return myStr;
    }

    /*
*  Convenience method to add a specified number of minutes to a Date object
*  From: http://stackoverflow.com/questions/9043981/how-to-add-minutes-to-my-date
*  @param  minutes  The number of minutes to add
*  @param  beforeTime  The time that will have minutes added to it
*  @return  A date object with the specified number of minutes added to it
*/
    public static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

//disables or enables all child controls in a view
    public static void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }

    public static String getCurrentTime() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    public static String formatEditedDate(String oldDate, int Hours, int Minutes, int Seconds) {

        //String dt = "2008-01-01";  // Start date
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // Calendar c = Calendar.getInstance();
       // c.setTime(sdf.parse(dt));
       // c.add(Calendar.DATE, 1);  // number of days to add
       // dt = sdf.format(c.getTime());  // dt is now the new date

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((cal.get(Calendar.HOUR_OF_DAY) > 20) && (Hours < 4)) {
            cal.add(Calendar.DATE, 1);
        }
        if ((cal.get(Calendar.HOUR_OF_DAY) < 4) && (Hours > 20)) {
            cal.add(Calendar.DATE, -1);
        }

        cal.set(Calendar.HOUR_OF_DAY, Hours);
        cal.set(Calendar.MINUTE, Minutes);
        cal.set(Calendar.SECOND, Seconds);

        return df.format(cal.getTime());

    }


    public static void TurnOffKeyboard(Activity activity, Context context) {
        //turn off keyboard:
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == activity.getCurrentFocus()) ? null : activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void ToggleKeyboard(Boolean OnOff, Context context) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (OnOff) {
            //True = On
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            //False = Off
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Date formatDateTime(String timeToFormat) {

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }
        }
        return date;
    }

    public static void TogglePeriodicSync(Account account, Context context) {

        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        Long syncInterval;

        if (globals.getBoolean("periodic_sync",true)) {
            syncInterval = globals.getLong("sync_interval",60L);

            ContentResolver.addPeriodicSync(
                    account,
                    AUTHORITY,
                    Bundle.EMPTY,
                    syncInterval);
        } else {
            ContentResolver.removePeriodicSync(
                    account,
                    AUTHORITY,
                    Bundle.EMPTY);
        }
    }

    public static void InitializeEntriesFragment(Context context, Activity activity, View rootView, String selection, Boolean leftJoin) {

        SearchView svEntries = (SearchView) rootView.findViewById(R.id.svEntries);
        Spinner spEntries = (Spinner) rootView.findViewById(R.id.spEntries);
        ListView lvEntries = (ListView) rootView.findViewById(R.id.lvEntries);

        InitializeEntriesSearchView(context, svEntries);
        InitializeEntriesSpinner(context, activity, spEntries);
        InitializeEntriesListView(context, lvEntries, selection, leftJoin, activity);
    }

    public static void InitializeEntriesListView(Context context, ListView lvEntries, String selection, Boolean leftJoin, Activity activity) {
        //Cursor cursorEntries = getEntriesInput(context,true,true,10,selection,"Time DESC");
        String[] projection = new String[]{"CPEntries.BIB","Time","TimeStamp","Synced","myEntry"};
        String table;
        if (leftJoin) {
            table = "ENTRIES_LEFTJOIN_ACTIVERACERS_LEFTJOIN_LOGININFO";
        } else {
            table = "ENTRIES_JOIN_ACTIVERACERS_JOIN_LOGININFO";
        }
        Cursor cursorEntries = getEntries(context,table,projection,selection,null,"Time DESC");
//Context context, String table, String[] projection, String selection, Integer limit,  String orderBy
        EntryObj[] EntryObjArray = new EntryObj[cursorEntries.getCount()];
        int i=0;
        cursorEntries.moveToFirst();
        while (!cursorEntries.isAfterLast()) {

            String BIB = cursorEntries.getString(0);
            String Time =  cursorEntries.getString(1);
            Long TimeStamp =  cursorEntries.getLong(2);
            String Synced = cursorEntries.getString(3);
            String myEntry = cursorEntries.getString(4);
            String Name = null;
            String LastName = null;
            String Country = null;
            int Age = 0;
            String Gender = null;


            Cursor cursorRacer = getActiveRacerIDFromRacers(context, BIB);
            if (cursorRacer.getCount() > 0) {
                cursorRacer.moveToFirst();
                Name = cursorRacer.getString(1);
                LastName = cursorRacer.getString(2);
                Country = cursorRacer.getString(3);
                Age = calculateAge(String.valueOf(cursorRacer.getString(5)));
                Gender = cursorRacer.getString(4);
            }
            cursorRacer.close();

            EntryObjArray[i] = new EntryObj();

            if (BIB != null) EntryObjArray[i].setBIB(BIB);
            else EntryObjArray[i].setBIB("");

            if (Time != null) EntryObjArray[i].setTime(Time);
            else EntryObjArray[i].setTime("");

            if (TimeStamp != null) EntryObjArray[i].setTimeStamp(TimeStamp);
            else EntryObjArray[i].setTimeStamp((long) 0);

            if (Name != null) EntryObjArray[i].setFirstName(Name);
            else EntryObjArray[i].setFirstName("");

            if (LastName != null) EntryObjArray[i].setLastName(LastName);
            else EntryObjArray[i].setLastName("");

            if (Country != null) EntryObjArray[i].setCountry(Country);
            else EntryObjArray[i].setCountry("");

            if (Age != 0) EntryObjArray[i].setAge(Age);
            else EntryObjArray[i].setAge(0);

            if (Gender != null) EntryObjArray[i].setGender(Gender);
            else EntryObjArray[i].setGender("");


            if (Synced != null) {
                if (Synced.equals("1")) EntryObjArray[i].setSynced(true);
                else EntryObjArray[i].setSynced(false);
            }
            else EntryObjArray[i].setSynced(false);

            if (myEntry != null) {
                if (myEntry.equals("1")) EntryObjArray[i].setMyEntry(true);
                else EntryObjArray[i].setMyEntry(false);
            }
            else EntryObjArray[i].setMyEntry(false);

            i++;
            cursorEntries.moveToNext();
        }
        cursorEntries.close();

        ListAdapter entriesAdapter = new EntriesListAdapter(context, EntryObjArray, activity, lvEntries);
        lvEntries.setAdapter(entriesAdapter);
    }



    private static void InitializeEntriesSpinner(Context context, Activity activity, Spinner spEntries) {

            /*

        Fill the spinner with:

        Default: Filter: All
        My Entries
        Synchronized
        Not Synchronized
        Race: <race1>
        Race: <race2>
        ...
        CP: <Treskavec>
        CP: <Prisad>
        ...

        */



        spEntries.setPrompt("Select Filter");

        //create source for the spinner
        ArrayList<String> list = new ArrayList<String>();
        list.add("All Entries");
        list.add("Deleted");
        list.add("Synchronized");
        list.add("Not Synchronized");

        //fill list with races
        Cursor racesCursor = getDistinctRacesFromLoginInfo(context);
        racesCursor.moveToFirst();
        while (!racesCursor.isAfterLast()) {
            list.add("Race | " + racesCursor.getString(2));
            racesCursor.moveToNext();

        }
        racesCursor.close();

        /*
        //fill list with Control points
        Cursor CPCursor = getDistinctCPFromEntries(context,"Valid = 1");
        CPCursor.moveToFirst();
        while (!CPCursor.isAfterLast()) {
            list.add("CP | " + CPCursor.getString(0) + " | " + CPCursor.getString(1));
            CPCursor.moveToNext();
        }
        CPCursor.close();
        */

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,R.layout.spinner_item,list);

        //spEntries.setDropDownViewResource(R.layout.setting_value_spinner_value‌​);


        spEntries.setAdapter(adapter);

        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        spEntries.setSelection(Entries.getSpinnerSelected(globals));
    }

    private static void InitializeEntriesSearchView(Context context, SearchView svEntries) {

    }

    public static String EncryptPassword(String Username, String Password) {
        String Salt = md5(Username);
        return md5(Password+Salt);
    }

    public static String md5(String input) {

        String md5 = null;

        if(null == input) return null;

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }


    public static EntryObj PrepareEntryObj(Context context, String inputedBIB, Integer EntryTypeID, String Barcode) {

        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        EntryObj entryObj = new EntryObj();
        entryObj.setEntryID(null);
        entryObj.setBIB(inputedBIB);
        Cursor cursorRacers = getActiveRacerIDFromRacers(context, inputedBIB);
        boolean racerFound = false;
        boolean cpnoSet = false;
        String raceID = "";

        if (cursorRacers.getCount() == 1) {
            racerFound = true;
            cursorRacers.moveToFirst();
            // ActiveRacerID, FirstName, LastName, Country, Gender, DateOfBirth
            entryObj.setActiveRacerID(Integer.parseInt(cursorRacers.getString(0)));
            entryObj.setFirstName(cursorRacers.getString(1));
            entryObj.setLastName(cursorRacers.getString(2));
            entryObj.setCountry(cursorRacers.getString(3));
            entryObj.setGender(cursorRacers.getString(4));
            entryObj.setAge(calculateAge(cursorRacers.getString(5)));
            raceID = cursorRacers.getString(6);
            entryObj.setRaceID(Integer.parseInt(raceID));

        } else {
            entryObj.setActiveRacerID(null);
            entryObj.setFirstName(null);
            entryObj.setLastName(null);
            entryObj.setCountry(null);
            entryObj.setGender(null);
            entryObj.setAge(0);
            entryObj.setRaceID(null);
        }

        cursorRacers.close();

        entryObj.setEntryTypeID(EntryTypeID);
        entryObj.setBarcode(Barcode);
        entryObj.setComment("");
        entryObj.setSynced(false);
        entryObj.setMyEntry(true);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date timeNow = calendar.getTime();
        String formattedDate = df.format(timeNow);
        entryObj.setTime(formattedDate); //example format: 2016-11-14 07:13:28
        entryObj.setTimeStamp(System.currentTimeMillis());

        //check if there is a previous entry close to this, whether to flag this entry valid true or false
        int timeBetweenEntries = globals.getInt("timebetweenentries",1);
        Date lastEntryDate = getDateForLastEntry(context, inputedBIB);
        if (lastEntryDate != null) {
            if (!(lastEntryDate.before(timeNow) && addMinutesToDate(timeBetweenEntries,lastEntryDate).after(timeNow))) {
           // if ((addMinutesToDate(timeBetweenEntries,lastEntryDate).before(timeNow)) && (lastEntryDate.after(timeNow))) {
                entryObj.setValid(true);
            } else {
                entryObj.setValid(false);
                entryObj.setReasonInvalid("Code 01: There was a valid entry for this runner in the last " + timeBetweenEntries + " minutes");
            }
        } else {
            entryObj.setValid(true);
        }



        if (globals.getBoolean("islogin",false)) {
            entryObj.setUserID(globals.getString("username",""));
            entryObj.setOperator(globals.getString("operator",""));
            String loginInfoWhere;
            String loginInfoOrder;
            if (racerFound) {
                loginInfoWhere = "RaceID='" + raceID + "'";
                loginInfoOrder = "CPNo DESC";
            } else {
                loginInfoWhere = "1";
                loginInfoOrder = null;
            }
            Cursor cursorCP = getEntryDataFromLoginInfo(context, loginInfoWhere,loginInfoOrder);
            //set CPNo:
            // 1. check from login info how many rows are there for this RaceID
            //2. If this CPNo exists check the next ... and so on... Raise alarm if all are used



            if (cursorCP.getCount() > 0) {
                cursorCP.moveToFirst();
                entryObj.setCPID(Integer.parseInt(cursorCP.getString(0)));
                entryObj.setCPName(cursorCP.getString(1));

                while (!cursorCP.isAfterLast()) {
                    String CPNo = cursorCP.getString(2);

                    if (!hasRunnerPassedThroughCP(context, inputedBIB,CPNo)) {
                        entryObj.setCPNo(cursorCP.getString(2));
                        cpnoSet = true;
                    }
                    cursorCP.moveToNext();
                }
                if (!cpnoSet) {

                    entryObj.setValid(false);
                    entryObj.setReasonInvalid("Code 02: The runner has already passed through this checkpoint " + cursorCP.getCount() + " times");
                }
            } else {
                entryObj.setCPID(null);
                entryObj.setCPName(null);
            }
            cursorCP.close();

        } else {
            entryObj.setCPID(null);
            entryObj.setCPName(null);
            entryObj.setUserID(null);
            entryObj.setOperator(null);
            entryObj.setCPNo(null);
        }



        return entryObj;
    }

    public static String ValidateEntry(Context context, EntryObj entryObj) {
        String status = "success";
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        if (!entryObj.isValid()) {
            if (entryObj.getReasonInvalid().startsWith("Code 01")) {
                status = "too soon";
                int timeBetweenEntries = globals.getInt("timebetweenentries",1);
                Toast.makeText(context, "There was a valid entry for this runner in the last " + timeBetweenEntries + " minutes", Toast.LENGTH_SHORT).show();
            }
            if (entryObj.getReasonInvalid().startsWith("Code 02")) {
                status = "already passed";
                Toast.makeText(context, "The runner has already passed through this checkpoint too many times times!", Toast.LENGTH_SHORT).show();
            }
        }

        if (!globals.getBoolean("islogin",false)) {
            if (!status.equals("too soon")) {
                status = "not logged in";
            }
        }
        return status;
    }

    public static int calculateAge(String DateOfBirth) {


        int age;
        int DOByear = Integer.parseInt(DateOfBirth.substring(0,4));
        int DOBmonth = Integer.parseInt(DateOfBirth.substring(5,7));
        int DOBday = Integer.parseInt(DateOfBirth.substring(8,10));

        final Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = 1 + calenderToday.get(Calendar.MONTH);
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);

        age = currentYear - DOByear;

        if(DOBmonth > currentMonth){
            --age;
        }
        else if(DOBmonth == currentMonth){
            if(DOBday > todayDay){
                --age;
            }
        }
        return age;
    }


}

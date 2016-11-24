package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    public static void InitializeInputFragment(Context context, View fragmentInput) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        TextView tvBIBEntry = (TextView) fragmentInput.findViewById(R.id.tvBIBEntry);
        if (!globals.contains("EntryNoState")) {
            SharedPreferences.Editor editor = globals.edit();
            editor.putString("EntryNoState","");
            editor.commit();
        }
        tvBIBEntry.setText(globals.getString("EntryNoState",""));
    }

    public static void PopulateRacesListView(Context context, ListView lvRacesLogin) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursorRaces = dbHelper.getDistinctRacesFromLoginInfo();
        final SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        RaceObj[] RacesObjArray = new RaceObj[cursorRaces.getCount()];
        int i=0;
        cursorRaces.moveToFirst();
        while(!cursorRaces.isAfterLast()) {

            final String RaceID = cursorRaces.getString(0);
            String RaceDescription = cursorRaces.getString(1);
            String CPNo = "";
            Cursor cursorCPNo = dbHelper.getDistinctCPNoFromLoginInfo("RaceID=" + RaceID);
            if (cursorCPNo != null && cursorCPNo.getCount()>0) {
                cursorCPNo.moveToFirst();
                while (!cursorCPNo.isAfterLast()) {
                    if (!CPNo.equals("")) {
                        CPNo += ", ";
                    }
                   CPNo += cursorCPNo.getString(0);
                    cursorCPNo.moveToNext();
                }
                cursorCPNo.close();
            }

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

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        final Cursor cursorDistinctRaces = dbHelper.getDistinctRacesFromLoginInfo();

        List<String> listDataHeader;
        HashMap<String, List<ActiveRacerObj>> listDataChild;
        ActiveRacersExpandableAdapter listAdapter;
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ActiveRacerObj>>();

        int i=0;
        cursorDistinctRaces.moveToFirst();
        while (!cursorDistinctRaces.isAfterLast()) {

            listDataHeader.add(cursorDistinctRaces.getString(1));
            Cursor cursorRacers = dbHelper.getActiveRacersForListView("RaceID=" + cursorDistinctRaces.getString(0));

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
                    child.setAge(cursorRacers.getString(4));
                } else  child.setAge("");
                if (cursorRacers.getString(5) != null && !cursorRacers.getString(5).equals("null")) {
                    child.setGender(cursorRacers.getString(5));
                } else  child.setGender("");

                Cursor lastEntryRow = dbHelper.getLastEntryRow(cursorRacers.getString(6));
                lastEntryRow.moveToFirst();
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


}

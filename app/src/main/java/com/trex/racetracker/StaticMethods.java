package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        TextView tvStatusBottom = (TextView) fragmentLogin.findViewById(R.id.tvStatusBottom);
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
        if (globals.getString("islogin","").equals("1")) loggedIn = true;
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

    public static void PopulateRacesListView(Context context, ListView lvRacesLogin) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursorRaces = dbHelper.getDistinctRacesFromLoginInfo();
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        RaceObj[] RacesObjArray = new RaceObj[cursorRaces.getCount()];
        int i=0;
        cursorRaces.moveToFirst();
        while(!cursorRaces.isAfterLast()) {

            String RaceID = cursorRaces.getString(0);
            String RaceDescription = cursorRaces.getString(1);
            boolean ShowRacers = true;

            if (globals.contains("showRacers" + RaceID)) {
                if (globals.getString("showRacers" + RaceID,"0").equals("0")) {
                    ShowRacers = false;
                }
            } else {
                //TODO - set for testing, this Toast should never happen!
                String toastText = "Warning! Global variable showRacers" + cursorRaces.getString(0) + "is not set properly! Contact the administrator!";
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            }

            RacesObjArray[i] = new RaceObj();
            RacesObjArray[i].setRaceID(RaceID);
            RacesObjArray[i].setRaceDescription(RaceDescription);
            RacesObjArray[i].setShowRacers(ShowRacers);

            i++;
            cursorRaces.moveToNext();

          }
        cursorRaces.close();

        ListAdapter racesAdapter = new RacesAdapter(context, RacesObjArray);
        lvRacesLogin.setAdapter(racesAdapter);
    }

    //Populate the listview in the Racers fragment
    public static void InitializeRacersFragment(Context context, View viewRacers, SharedPreferences globals) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursorRacers = dbHelper.getActiveRacersForListView("");
        ActiveRacerObj[] ActiveRacersObjArray = new ActiveRacerObj[cursorRacers.getCount()];
        int i=0;
        cursorRacers.moveToFirst();
        while (!cursorRacers.isAfterLast()) {
            //populate array
            ActiveRacersObjArray[i] = new ActiveRacerObj();
            if (cursorRacers.getString(0) != null && !cursorRacers.getString(0).equals("null")) {
                ActiveRacersObjArray[i].setBIB(cursorRacers.getString(0));
            } else  ActiveRacersObjArray[i].setBIB("");
            if (cursorRacers.getString(0) != null && !cursorRacers.getString(1).equals("null")) {
                ActiveRacersObjArray[i].setFirstName(cursorRacers.getString(1));
            } else  ActiveRacersObjArray[i].setFirstName("");
            if (cursorRacers.getString(0) != null && !cursorRacers.getString(2).equals("null")) {
                ActiveRacersObjArray[i].setLastName(cursorRacers.getString(2));
            } else  ActiveRacersObjArray[i].setLastName("");
            if (cursorRacers.getString(0) != null && !cursorRacers.getString(3).equals("null")) {
                ActiveRacersObjArray[i].setCountry(cursorRacers.getString(3));
            } else  ActiveRacersObjArray[i].setCountry("");
            if (cursorRacers.getString(0) != null && !cursorRacers.getString(4).equals("null")) {
                ActiveRacersObjArray[i].setAge(cursorRacers.getString(4));
            } else  ActiveRacersObjArray[i].setAge("");
            if (cursorRacers.getString(0) != null && !cursorRacers.getString(5).equals("null")) {
                ActiveRacersObjArray[i].setGender(cursorRacers.getString(5));
            } else  ActiveRacersObjArray[i].setGender("");

            Cursor lastEntryRow = dbHelper.getLastEntryRow(cursorRacers.getString(6));
            if (lastEntryRow.getCount()>0) {
                if (lastEntryRow.getString(0) != null && !lastEntryRow.getString(0).equals("null")) {
                    ActiveRacersObjArray[i].setTimeLast(lastEntryRow.getString(0));
                } else {
                    ActiveRacersObjArray[i].setTimeLast("");
                }

                String CPNoString = "";
                if (lastEntryRow.getString(1) != null && !lastEntryRow.getString(1).equals("null")) {
                    CPNoString += "CP " + lastEntryRow.getString(1);
                }
                ActiveRacersObjArray[i].setCPNo(CPNoString);

                if (lastEntryRow.getString(2) != null && !lastEntryRow.getString(2).equals("null")) {
                    ActiveRacersObjArray[i].setCPName(lastEntryRow.getString(2));
                }

            } else {
                ActiveRacersObjArray[i].setTimeLast("");
                ActiveRacersObjArray[i].setCPNo("");
                ActiveRacersObjArray[i].setCPName("");
            }

            i++;
            cursorRacers.moveToNext();
        }
        cursorRacers.close();

        ListAdapter racersAdapter = new ActiveRacersAdapter(context, ActiveRacersObjArray);
        ListView lvRacers = (ListView) viewRacers.findViewById(R.id.lvRacers);
        lvRacers.setAdapter(racersAdapter);
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

}
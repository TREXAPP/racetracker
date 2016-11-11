package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by Igor_2 on 03.11.2016.
 */

public class Methods {

    public Methods() {
        //default constructor
    }


    public void InitializeSyncFragment(Context ctx, View rootview, SharedPreferences globals) {

        FrameLayout fragmentSync = (FrameLayout) rootview.findViewById(R.id.fragmentSync);
        TextView tvStatusTop = (TextView) rootview.findViewById(R.id.tvStatusTop);
        EditText etUsername = (EditText) rootview.findViewById(R.id.etUsername);
        EditText etPassword = (EditText) rootview.findViewById(R.id.etPassword);
        EditText etOperator = (EditText) rootview.findViewById(R.id.etOperator);
        Button btnLogin = (Button) rootview.findViewById(R.id.btnLogin);
        Button btnLogout = (Button) rootview.findViewById(R.id.btnLogout);
        TextView tvStatusBottom = (TextView) rootview.findViewById(R.id.tvStatusBottom);

        Boolean loggedIn = false;
        globals = ctx.getSharedPreferences(MainActivity.GLOBALS,0);
        String Username = globals.getString("username","");
        String Operator = globals.getString("operator","");
        String controlPoint = globals.getString("controlpoint","");
        if (globals.getString("islogin","").equals("1")) loggedIn = true;
        if (loggedIn) {
            //user is logged in. Show info and form for logout
            String displayStatusTop = "Logged in user: " + Username;
            if (!Operator.equals("")) {
                displayStatusTop += " (";
                displayStatusTop += Operator;
                displayStatusTop += ")";
            }
            displayStatusTop += "\n" + "Control Point: " + controlPoint;

            tvStatusTop.setText(displayStatusTop);
            etUsername.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            etOperator.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
         //   tvStatusTop.setTextColor(ctx.getResources().getColor(android.R.color.darker_gray));
            tvStatusTop.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary));

        } else {
            //user is not logged, show for for logging in
            String displayStatusTop = "Not logged in";
            tvStatusTop.setText(displayStatusTop);
            tvStatusTop.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etOperator.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    //Populate the listview in the Racers fragment
    public void InitializeRacersFragment(Context context, View viewRacers, SharedPreferences globals) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursorRacers = dbHelper.getActiveRacersForListView("");
        ActiveRacer[] ActiveRacersArray = new ActiveRacer[cursorRacers.getCount()];
        int i=0;
        cursorRacers.moveToFirst();
        while (!cursorRacers.isAfterLast()) {
            //pupulate array
            ActiveRacersArray[i] = new ActiveRacer();
            if (cursorRacers.getString(0) != null || cursorRacers.getString(0).equals("null")) {
                ActiveRacersArray[i].setBIB(cursorRacers.getString(0));
            } else  ActiveRacersArray[i].setBIB("");
            if (cursorRacers.getString(0) != null || cursorRacers.getString(1).equals("null")) {
                ActiveRacersArray[i].setFirstName(cursorRacers.getString(1));
            } else  ActiveRacersArray[i].setFirstName("");
            if (cursorRacers.getString(0) != null || cursorRacers.getString(2).equals("null")) {
                ActiveRacersArray[i].setLastName(cursorRacers.getString(2));
            } else  ActiveRacersArray[i].setLastName("");
            if (cursorRacers.getString(0) != null || cursorRacers.getString(3).equals("null")) {
                ActiveRacersArray[i].setCountry(cursorRacers.getString(3));
            } else  ActiveRacersArray[i].setCountry("");
            if (cursorRacers.getString(0) != null || cursorRacers.getString(4).equals("null")) {
                ActiveRacersArray[i].setAge(cursorRacers.getString(4));
            } else  ActiveRacersArray[i].setAge("");
            if (cursorRacers.getString(0) != null || cursorRacers.getString(5).equals("null")) {
                ActiveRacersArray[i].setGender(cursorRacers.getString(5));
            } else  ActiveRacersArray[i].setGender("");

            Cursor lastEntryRow = dbHelper.getLastEntryRow(cursorRacers.getString(6));
            if (lastEntryRow.getCount()>0) {
                if (lastEntryRow.getString(0) != null || lastEntryRow.getString(0).equals("null")) {
                    ActiveRacersArray[i].setTimeLast(lastEntryRow.getString(0));
                } else {
                    ActiveRacersArray[i].setTimeLast("");
                }

                String CPString = "";
                if (lastEntryRow.getString(1) != null || lastEntryRow.getString(1).equals("null")) {
                    CPString += "CP " + lastEntryRow.getString(1);
                }
                if (lastEntryRow.getString(2) != null || lastEntryRow.getString(2).equals("null")) {
                    CPString += " - " + lastEntryRow.getString(2);
                }
                ActiveRacersArray[i].setCPLast(CPString);
            } else {
                ActiveRacersArray[i].setTimeLast("");
                ActiveRacersArray[i].setCPLast("");
            }

            i++;
            cursorRacers.moveToNext();
        }
        cursorRacers.close();

        ListAdapter racersAdapter = new ActiveRacersAdapter(context, ActiveRacersArray);
        ListView lvRacers = (ListView) viewRacers.findViewById(R.id.lvRacers);
        lvRacers.setAdapter(racersAdapter);
    }

    public String FormatErrorString(String str) {
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

package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
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

    public void InitializeRacersFragment(Context context, View viewRacers, SharedPreferences globals) {
        String[] racersString = {"racer1","racer2","racer3","racer4","racer5","racer6","racer7"};
        ListAdapter racersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, racersString);
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

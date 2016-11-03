package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

        } else {
            //user is not logged, show for for logging in
            String displayStatusTop = "Not logged in";
            tvStatusTop.setText(displayStatusTop);
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etOperator.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

}
